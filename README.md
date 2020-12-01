# swing-mvvm
Offers mvvm functionality to the java swing ecosystem.

It is achieved by defining Bindings between the UI components and properties.
You can create Bindings between almost anything, but for ease of use, there are Properties.

## Properties
Properties are an easy way to create value containers that can be bound to a component, and that will 
automatically update.

Example:

```
Property<String> nameProperty = PropertyFactory.createProperty("name", this, String.class);
```
or
```
Property<String> nameProperty = PropertyFactory.createProperty("name", this, "Initial value");
```
Where:
* name: is the name of the property, and will also be the propert name in change events, when the value of the property changes.
* this: the owner of the property, and will be the owner in change events.
* String.class: the type of value the property can hold. 

You can get the actual value of a Property using ```nameProperty.get()``` and assign a property to it with ```nameProperty.set("New name")```
(There are also methods to check whether it contains a value or not)

You can register a PropertyChangeListener so that you're notified whenever the value of the property changes. 

## Bindings

The easiest way to create bindings is by using the ```@Bind``` annotations.

Example:
```
public class MyUi extends JPanel {
  @Bind("text", target = "inputText.value")
  private JTextField input = new JTextField();

  @Bind("selected", target = "activate.value")
  private JCheckBox activate = new JCheckBox("Activate");
  ...
}

public class MyViewModel {
  private Property<String> inputText = PropertyFactory.createProperty("Input", this, String.class);
  private Property<Boolean> activate = PropertyFactory.createProperty("Activate", this, Boolean.class);
  ...
}

MyUi ui = new MyUi();
MyViewModel vm = new MyViewModel();
Binder.bind(ui, vm);
```
This will bind the text of the JTextField to the value of the inputText property, and the 'checked' state of the checkbox to the value of the activate property.

If you prefer to put the annotations on the view model, you can do it like this:

```
public class MyUi extends JPanel {
  private JTextField input = new JTextField();
  private JCheckBox activate = new JCheckBox("Activate");
  ...
}

public class MyViewModel {
  @Bind("value", target = "input.text")
  private Property<String> inputText = PropertyFactory.createProperty("Input", this, String.class);
  @Bind("value", target = "activate.selected")
  private Property<Boolean> activate = PropertyFactory.createProperty("Activate", this, Boolean.class);
  ...
}

MyViewModel vm = new MyViewModel();
MyUi ui = new MyUi();
Binder.bind(vm, ui);
```
Basically the first object that is passed to the Binder.bind method needs to have the annotations. 
You can call Binder.bind multiple times with different combinations of source/target.

You can also use multiple instances of ```@Bind``` on a field: 

```
public class MyUi extends JPanel {
  @Bind("text", target = "inputText.value")
  @Bind("editable", target = "changeable.value", type = BindingType.TARGET_TO_SOURCE)
  private JTextField input = new JTextField();

  ...
}
public class MyViewModel {
  private Property<String> inputText = PropertyFactory.createProperty("Input", this, String.class);
  private Property<Boolean> changeable = PropertyFactory.createProperty("Changeable", this, Boolean.class);
  ...
}

MyUi ui = new MyUi();
MyViewModel vm = new MyViewModel();
Binder.bind(ui, vm);
```

The @Bind path of the source (the value of the @Bind annotation) points to a field (or method) of the field type.
Remark that some path are predefined and do more than just point to a field or method. You can find the list of predefined paths in the Paths interface.

The target path, points to a nested field or method of the object that is passed to Binder.bind(source, target); 


This will make the JTextField editable when the changeable property has a value = true.

BindingTypes:

* SOURCE_TO_TARGET (default): applies the value of the source to the target whenever the source changes.
* TARGET_TO_SOURCE : applies the value of the target to the source, whenever the target changes.
* BI_DIRECTIONAL: applies the value of the changing object to the other one.

Source and target can be the same object:

```
public class MyUi extends JPanel {
  @Bind("get", target = "input2.text", type = BindingType.BI_DIRECTIONAL)
  private JTextField input = new JTextField();

  private JTextField input2 = new JTextField();

  ...
}
MyUi ui = new MyUi();
Binder.bind(ui, ui);
```
This will result in 2 input fields that have their text synchronized.

The paths can be nested:

```
public class MyUi extends JPanel {
  @Bind("text", target = "inputText", type = BindingType.BI_DIRECTIONAL)
  @Bind("editable", target = "subView.changeable.value", type = BindingType.TARGET_TO_SOURCE)
  private JTextField input = new JTextField();
  ...
}

public class MyViewModel {
  private Property<String> inputText = PropertyFactory.createProperty("Input", this, String.class);
  private SubViewModel subview = new SubViewModel();
  ...
}

public class SubViewModel {
  private Property<Boolean> changeable = PropertyFactory.createProperty("Changeable", this, Boolean.class);
  ...
}

MyUi ui = new MyUi();
MyViewModel model = new MyViewModel();
Binder.bind(ui, model);
```

## Manual Bindings
To create a Binding manually you use a BindingBuilder:

```
Property<String> nameProperty = PropertyFactory.createProperty("name", this, String.class);
JLabel label = new JLabel();
Binding binding = new BindingBuilder<String, String>()
     .withSourceSupplier(nameProperty::get)
     .withSourceTrigger((b, d) -> nameProperty.addPropertyChangeListener(e -> b.apply(d)))
     .withTargetConsumer(s -> label.setText(s))
     .build();

Property<Boolean> allowEditProperty = PropertyFactory.createProperty("allowEdit", this, false);
JTextField field = new JTextField();
new BindingBuilder<Boolean, Boolean>()
  .withSourceSupplier(allowEditProperty::get)
  .withSourceTrigger((b, d) -> allowEditProperty.addPropertyChangeListener(e -> b.apply(d)))
  .withTargetConsumer(b -> field.setEditable(b))
  .build();
```
This creates a uni-directional Binding between the nameProperty and a JLabel, meaning that everytime the property value changes, the label will be updated.

You can change this around:
```
JTextField field = new JTextField();
Property<String> userId = PropertyFactory.createProperty("userid", this, String.class);
new BindingBuilder<String, String>()
  .withSourceSupplier(() -> field.getText())
  .withSourceTrigger(new DocumentTextChangedTrigger(field))
  .withTargetConsumer(userId::set)
  .build();
```
So, now everytime the text in the text field changes, the value will be pushed to the userId property.

You can make the Binding bidirectional like this:
```
JTextField field = new JTextField();
Property<String> userId = PropertyFactory.createProperty("userid", this, String.class);
new BindingBuilder<String, String>()
  .withSourceSupplier(() -> field.getText())
  .withSourceTrigger(new DocumentTextChangedTrigger(field.getDocument()))
  .withSourceConsumer(s -> field.setText(s))
  .withTargetConsumer(userId::set)
  .withTargetSupplier(userId::get)
  .withTargetTrigger((b, d) -> userId.addPropertyChangeListener(e -> b.apply(d))
  .build();
```
Now both the text field, and the property will be synchronized, not matter which one changes.

It is possible to replace the ```...Trigger((b, d) -> userId.addPropertyChangeListener(e -> b.apply(d))``` with ```...Trigger(new PropertyTrigger(property))```

## ObservableCollections
Observable collections are collections that will fire events every time the content changes. (Elements are added, removed, replaced).

You can instantiate them using:
```
ObservableCollection<String> col = ObservableCollectionFactory.createCollection();
```
### Collection views
To create a view on an ObservableCollection use the:
```
ObservableCollection<T> col = ObservableCollectionFactory.createCollection(ObservableCollection<T>, Comparator<T>);
```
for a sorted view, and
```
ObservableCollection<T> col = ObservableCollectionFactory.createCollection(ObservableCollection<T>, Predicate<T>);
```
for a filtered view.

To have a filtered, sorted view:
```
ObservableCollection<T> col = ObservableCollectionFactory.createCollection(ObservableCollection<T>, Comparator<T>);
col = ObservableCollectionFactory.createCollection(col, Predicate<T>);
```

## Triggers
Triggers are the classes that will invoke a Binding when triggered. This can be when a property changes, an action has been performed, or any other event.

Available Triggers:

Name | Description
---- | ----------- 
PropertyTrigger | applies the binding when the associated property changes
DocumentTextChangedTrigger | applies the binding when the text of the associated document changes
ObservableCollectionTrigger | applies the binding whenever the associated collection changes
ComponentChangedTrigger | applies the binding whenever the associated JComponent fires a specific change event.
ItemSelectedTrigger | applies the binding whenever the associated AbstractButton selection state changes.
ListSelectionTrigger | applies the binding whenever the associated JList or JTables selection changes.

Triggers used by annotation bindings can be added using the TriggerFactory.registerTriggerFactory methods.

## ValueSuppliers
ValueSuppliers are the classes that will provide a value when a Binding is applied.

The ValueSupplierFactory registers several suppliers, and additional ones can be added using the ValueSupplierFactory.registerSupplierFactory methods.


## ValueConsumers
ValueConsumers are the classes that will consume the value when a Binding is applied.

The ValueConsumerFactory registers several consumers, and additional ones can be added using the ValueConsumerFactory.registerConsumerFactory.

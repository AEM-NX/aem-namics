#Granite UI Validators

https://github.com/AEM-NX/aem-namics/tree/master/aem-namics-examples/granite-ui-validators

After deploying the clientlib, use the validator like following example in your dialogs:

```html
<with jcr:primaryType="nt:unstructured"
    sling:resourceType="granite/ui/components/foundation/form/textfield"
    value="100%" required="true" fieldLabel="Label"
    fieldDescription="Description"
    name="./width" regex="^[0-9%]*$"
    regex-text="Only numbers and % are allowed." />
```

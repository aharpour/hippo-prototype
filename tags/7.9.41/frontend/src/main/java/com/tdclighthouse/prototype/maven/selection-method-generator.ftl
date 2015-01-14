public ${type} get${methodName}() {
    if (this.${fieldName} == null) {
        this.${fieldName} = getSelectionBean("${propertyName}", "${valueListPath}");
    }
    return this.${fieldName};
}
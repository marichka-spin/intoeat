package net.lviv.intoeat.validation;

public interface BaseValidator<T> {

    String MANDATORY_PARAMETERS_MISSING_ERROR_MSG = "One or more mandatory parameters are missing.";
    String MANDATORY_PARAMETER_NAME_MISSING_ERROR_MSG = "Mandatory parameter 'name' is missing.";
    String ENTITY_WITH_NAME_EXISTS_ERROR_MSG = "%s with name: %s already exists.";

    void validate(T type);

    String type();

}

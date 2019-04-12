package net.lviv.intoeat.validation;

public interface RemovalValidator {

    String NOTHING_TO_DELETE_ERROR_MSG = "Object id is null. Nothing to delete.";
    String ENTITY_WITH_ID_NOT_FOUND_ERROR_MSG = "%s with id %d not found.";

    public void validate(Integer id);

}

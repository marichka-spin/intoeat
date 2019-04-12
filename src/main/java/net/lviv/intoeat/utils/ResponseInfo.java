package net.lviv.intoeat.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseInfo {

    private Integer objectId;
    private Status status;
    private String message;

    public ResponseInfo() {
        this.status = Status.SUCCESS;
    }

    public ResponseInfo(Integer objectId) {
        this.objectId = objectId;
    }

    public ResponseInfo(String message) {
        this.message = message;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        SUCCESS("success"), FAILED("failure");

        private String state;

        Status(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }
    }
}

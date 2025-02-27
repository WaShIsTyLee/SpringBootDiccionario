package org.example.diccionario2.Exception;

public class RecordNotFoundException extends RuntimeException {
  private String exceptionDetail;
  private Object fieldValue;

    public RecordNotFoundException(String exceptionDetail, Object fieldValue) {
        super(exceptionDetail + fieldValue);
        this.exceptionDetail = exceptionDetail;
        this.fieldValue = fieldValue;
    }

    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}

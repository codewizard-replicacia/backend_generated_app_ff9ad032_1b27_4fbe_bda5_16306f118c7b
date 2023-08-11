package com.mycompany.group234.Exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
  private String errorMessage;
  private String errorCode;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private Date timestamp;
  public Date getTimestamp() {
	return timestamp;
  }
  public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
  }
  public String getErrorMessage() {
	return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
	 this.errorMessage = errorMessage;
  }
  public String getErrorCode() {
	  return errorCode;
  }
  public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
  }
  
  
}

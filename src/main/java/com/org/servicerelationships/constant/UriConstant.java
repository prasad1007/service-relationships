package com.org.servicerelationships.constant;

public final class UriConstant {

  private UriConstant() {
    throw new UnsupportedOperationException(GlobalConstants.CONSTANTS_CLASS_DONT_CONSTRUCT);
  }

  public static final String API = "servicerelationships";

  public static final String UPLOAD_CSV = "/uploadCsv";

  public static final String GET_ALL_SERVICE_RELATIONSHIPS = "/getAllServiceRelationships";

  public static final String GET_SERVICE_RELATIONSHIPS_BY_PARENT_AND_CHILD =
      "/getServiceRelationshipsByParentAndChild/{parent}/{child}";

  public static final String COMPARE_AND_UPDATE = "/compareAndUpdate";
}

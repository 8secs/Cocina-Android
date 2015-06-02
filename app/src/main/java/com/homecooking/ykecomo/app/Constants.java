package com.homecooking.ykecomo.app;


public class Constants {

    public static String IS_FIRST_TIME_STR = "is_first_time";
    public static String FB_NAMESPACE = "pruebamisplatos";
    public static final String IS_FRONT_CAMERA = "is_front_camera";
    public static String LOCALE_ES = "es";

    /**
     * PROFILE ITEMS
     */

    public static String TYPE_USER = "type_user";
    public static int FB_USER_TYPE = 2;
    public static int LOGIN_USER_TYPE = 1;
    public static int VIEW_PROFILE_OBSERVABLE_TYPE = 1;
    public static int EDIT_PROFILE_OBSERVABLE_TYPE = 2;

    public static String EDIT_PROFILE_ITEMS = "edit_profiles_items";
    public static int EDIT_NAME_MEMBER_PROFILE = 1;
    public static int EDIT_EMAIL_MEMBER_PROFILE = 2;
    public static int EDIT_DESC_MEMBER_PROFILE = 3;
    public static int EDIT_ADDRESS_MEMBER_PROFILE = 4;

    public static String USER_ENVIRONMENT = "user_environment";
    public static int USER_ENVIRONMENT_MODE = 1;
    public static int CHEF_ENVIROMENT_MODE = 2;


    /**
     * MEMBER
     */

    public static String IS_MEMBER = "is_member";
    public static String IS_FB_MEMBER = "is_fb_member";
    public static String MEMBER_NAME = "member_name";
    public static String MEMBER_SURNAME = "member_surname";
    public static String MEMBER_EMAIL = "member_email";
    public static String MEMBER_VERIFIED = "member_verified";
    public static String MEMBER_DESCRIPTION = "member_description";
    public static String MEMBER_ADDRESS = "member_address";
    public static String MEMBER_CITY = "member_city";
    public static String MEMBER_STATE = "member_state";
    public static String MEMBER_COUNTRY = "member_country";
    public static String MEMBER_DIRECCION = "member_direccion";
    public static String MEMBER_POSTAL_CODE = "member_postal_code";
    public static String MEMBER_HAS_ADDRESS = "member_has_address";
    public static String MEMBER_ID = "member_id";
    public static String MEMBER_IMAGE = "member_image";
    public static String MEMBER_FB_ID = "member_fb_id";

    /**
     * PRODUCT
     */
    public static String PRODUCT_ITEMS = "product_items";
    public static int ADD_PRODUCT_ITEM  = 0;
    public static int EDIT_PRODUCT_ITEM = 1;

    public static String COLUMN_PRODUCT_ITEMS = "column_product";
    public static int TITLE_COLUMN_ITEM = 1;
    public static int PORTIONS_COLUMN_ITEM = 2;
    public static int PRICE_COLUMN_ITEM = 3;
    public static int DESC_COLUMN_ITEM = 4;
    public static int CONT_COLUMN_ITEM = 5;
    public static int CATEGORY_COLUMN_ITEM = 6;
    public static int IMAGE_COLUMN_ITEM = 7;



    /**
     * URL STRINGS
     */

    public static final String PENDING_ACTION_BUNDLE_KEY = "com.homecooking.ykecomo:PendingAction";
    public static final String BASE_URL = "http://cocina.visitaelaljarafe.com/";
    public static final String GRAPH_FB_URL = "https://graph.facebook.com/";
    public static final String PICTURE_FB_URL_PARAMS = "/picture/?type=large";
    public static final String DEFAULT_AVATAR_CHEF = "assets/Uploads/cooker1.png";
    public static final String UPLOAD_MIME_TYPE = "application/octet-stream";
    public static final String UPLOADS_URL = BASE_URL+"assets/Uploads/";



    public static final String CARGANDO_DATOS = "Cargando datos...";

    /**
     * MAPS
     */
    public static final String LAST_KNOWN_LOCATION = "lastLocation";
    public static final String LOCATIONS_UPDATES = "locationUpdates";
    public static final String NON_COUNTRY_CODE = "non_country_code";
    public static final String NON_COUNTRY_NAME = "non_country_name";



    /**
     * REQUEST CODES
     */

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int EDIT_PROFILE_REQUEST_CODE = 2;
    public static final int EDIT_PRODUCT_REQUEST_CODE = 3;


    /**
     * BUNDLES STRINGS
     */

    public static final String TITLE_BUNDLE_KEY = "title";
    public static final String ID_BUNDLE_KEY = "id";
    public static final String PRODUCTID_BUNDLE_KEY = "productID";
    public static final String AVATAR_BUNDLE_KEY = "avatar";
    public static final String MEMBER_ADDRESS_BUNDLE_KEY = "address";

    /**
     * PARAMS MEMBER
     */

    public static final String FIRST_NAME = "FirstName";
    public static final String SURNAME = "Surname";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String FACEBOOK_UID = "FacebookUID";
    public static final String FACEBOOK_LINK = "FacebookLink";
    public static final String DESCRIPTION = "Description";
    public static final String DEFAULT_SHIPPING_ADDRESS = "DefaultShippingAddressID";
    public static final String AVATAR_ID = "AvatarID";

    /**
     * PARAMS ADDRESS
     */
    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String STATE = "State";
    public static final String ADDRESS = "Address";
    public static final String POSTAL_CODE = "PostalCode";
    public static final String MEMBERID = "MemberID";

    /**
     * PARAMS IMAGE
     */

    public static final String FILENAME = "Filename";
    public static final String NAME = "Name";
    public static final String PARENT = "Parent";

    /**
     * PARAMS PRODUCT
     */

    public static final String TITLE = "Title";
    public static final String CATEGORY = "Category";
    public static final String CATEGORIES = "Categories";
    public static final String PRICE = "BasePrice";
    public static final String PORTIONS = "Portions";
    public static final String MINI_DESCRIPTION = "MiniDescription";
    public static final String MODEL = "Model";
    public static final String CONTENT = "Content";
    public static final String CHEF_ID = "ChefID";
    public static final String IMAGE = "Image";
    public static final String IMAGE_ID = "ImageID";
    public static final String PARENT_ID = "ParentID";
    public static final String ALLOW_PURCHASE = "AllowPurchase";

}

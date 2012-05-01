package proj3.client;

import com.google.gwt.core.client.JavaScriptObject;
class User extends JavaScriptObject
{
   protected User()
   {}
   public final native int getID()
   /*-{
      return this.user.id;
   }-*/;
   public final native String getFirstName()
   /*-{
      return this.user.first_name;
   }-*/;
   public final native String getLastName()
   /*-{
      return this.user.last_name;
   }-*/;
   public final native String getUserName()
   /*-{
      return this.user.username;
   }-*/;
   public final native String getDepartment()
   /*-{
      return this.user.department;
   }-*/;
   public final native String getDivsion()
   /*-{
      return this.user.division;
   }-*/;
}


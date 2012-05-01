package proj3.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.JsonUtils;
import java.util.ArrayList;

import proj3.client.User;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InsertPanel.ForIsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ListBox;

public class Proj3 implements EntryPoint, ClickHandler
{
   VerticalPanel mainPanel = new VerticalPanel();
   String baseURL = "http://localhost:3000";
   TextBox userBox = new TextBox();
   PasswordTextBox passBox = new PasswordTextBox();
   Button loginButton = new Button("Login");
   Button addButton = new Button("Create Survey");
   Button viewResults = new Button("View Results");
   Button returnMain = new Button("Return to main page");
   Button logoutButton = new Button("Logout");
   Button chooseResults = new Button("View Results");
   int id;
   ListBox surveyListBox;
   
   ArrayList<MyUser> users;
   JsArray<User> jsonData;
   
   private static class MyUser
   {
      private int id;
      private String first_name;
      private String last_name;
      private String username;
      private String department;
      private String division;
      
      public MyUser(int id, String fn, String ln, String user, String dept, String div)
      {
         this.id = id;
         this.first_name = fn;
         this.last_name = ln;
         this.username = user;
         this.department = dept;
         this.division = div;
      }
   }

   public void onModuleLoad()
   {
	  setupLogin();
	  loginButton.addClickHandler(this);
	  addButton.addClickHandler(this);
	  viewResults.addClickHandler(this);
	  returnMain.addClickHandler(this);
	  logoutButton.addClickHandler(this);
	  Frame frame = new Frame("http://localhost:3000/questions/chooseSurvey");
	  frame.setWidth("100%");
	  frame.setHeight("450px");
	  mainPanel.add(frame);
	  RootPanel.get().add(mainPanel);

   }
   public void onClick(ClickEvent e)
   {
	   
	   Object source = e.getSource();
	   if (source == loginButton) {
		   String url = baseURL + "/pages/login";
		   String postData = URL.encode("username") + "=" + URL.encode(userBox.getText().trim()) + "&" +
				   URL.encode("password") + "=" + URL.encode(passBox.getText().trim());
		   userBox.setText("");
		   passBox.setText("");
		   postRequest(url,postData,"postLogin");
	   }
	   else if (source == addButton) {
			Frame frame = new Frame("http://localhost:3000/questions/createSurvey");
			frame.setWidth("100%");
			frame.setHeight("450px");
			mainPanel.clear();
		    mainPanel.add(returnMain);
		    HorizontalPanel hPanel = new HorizontalPanel();
			mainPanel.add(hPanel);
			hPanel.add(frame);
			hPanel.add(returnMain);			
	   }
	   else if (source == viewResults) {
	     	String url = baseURL + "/survey/showSurvey";
	     	String postData = URL.encode("user_id") + "=" +
	     			URL.encode("" + id);
	     		postRequest(url,postData,"getSurveyListBox");
	   }
	   else if (source == chooseResults) {
		   String survey_name = surveyListBox.getValue(surveyListBox.getSelectedIndex());
		   //surveyChart(survey_name);
	   }
	   else if (source == returnMain){
		   loginPanel();
	   }
	   else if (source == logoutButton){
		   setupLogin();
	   }
		   
	}


   public void getRequest(String url, final String getType) {
      final RequestBuilder rb = new
         RequestBuilder(RequestBuilder.GET,url);
      try {
         rb.sendRequest(null, new RequestCallback()
         {
            public void onError(final Request request,
               final Throwable exception)
            {
               Window.alert(exception.getMessage());
            }
            public void onResponseReceived(final Request request,
               final Response response)
            {
            	if (getType.equals("getUsers")) {
            		showUsers(response.getText());
            	}
            }
         });
      }
      catch (final Exception e) {
         Window.alert(e.getMessage());
      }
   } // end getRequest()
   public void postRequest(String url, String data, final String postType)
   {
	   final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST,url);
	   rb.setHeader("Content-type","application/x-www-form-urlencoded");
	   try {
		   rb.sendRequest(data, new RequestCallback()
		   {
			   public void onError(final Request request, final Throwable exception)
			   {
				   Window.alert(exception.getMessage());
			   }
			   public void onResponseReceived(final Request request, final Response response)
			   {
				   if (postType.equals("postLogin")) {
					   mainPanel.clear();
				   	   int id = Integer.parseInt(response.getText());
				   	   if (id == 1) {
				   		   mainPanel.clear();
				   		   VerticalPanel adminPanel = new VerticalPanel();
				   		   Label adminLabel = new Label("Admin Login");
				   		   HorizontalPanel adminRow = new HorizontalPanel();
				   		   adminRow.add(adminLabel);
				   		   adminPanel.add(adminRow);
				   		   adminRow.add(logoutButton);
				   		   mainPanel.add(adminPanel);	
						   String url = baseURL + "/users/index.json";
						   getRequest(url, "getUsers");
				   	   }
				   	   else if (id == 2) {
				   		   mainPanel.clear();
				   		   VerticalPanel divHeadPanel = new VerticalPanel();
				   		   Label divHeadLabel = new Label("Div Head Logged In");
				   		   HorizontalPanel divHeadRow = new HorizontalPanel();
				   		   divHeadRow.add(divHeadLabel);
				   		   divHeadPanel.add(divHeadRow);
				   		   divHeadRow.add(addButton);
				   		   divHeadRow.add(viewResults);
				   		   divHeadRow.add(logoutButton);
				   		   mainPanel.add(divHeadPanel);
				   	   }
				   	   else if (id == 3) {
				   		   mainPanel.clear();
				   		   VerticalPanel deptAHeadPanel = new VerticalPanel();
				   		   Label deptAHeadLabel = new Label("DeptA Head Logged In");
				   		   HorizontalPanel deptAHeadRow = new HorizontalPanel();
				   		   deptAHeadRow.add(deptAHeadLabel);
				   		   deptAHeadPanel.add(deptAHeadRow);
				   		   deptAHeadRow.add(addButton);
				   		   deptAHeadRow.add(viewResults);
				   		   deptAHeadRow.add(logoutButton);
				   		   mainPanel.add(deptAHeadPanel);
				   	   }
				   	   else if (id == 4) {
				   		   mainPanel.clear();
				   		   VerticalPanel deptBHeadPanel = new VerticalPanel();
				   		   Label deptBHeadLabel = new Label("DeptB Head Logged In");
				   		   HorizontalPanel deptBHeadRow = new HorizontalPanel();
				   		   deptBHeadRow.add(deptBHeadLabel);
				   		   deptBHeadPanel.add(deptBHeadRow);
				   		   deptBHeadRow.add(addButton);
				   		   deptBHeadRow.add(viewResults);
				   		   deptBHeadRow.add(logoutButton);
				   		   mainPanel.add(deptBHeadPanel);
				   	   }
				   	   else if (id > 4) {
				   		   mainPanel.clear();
				   		   VerticalPanel userPanel = new VerticalPanel();
				   		   Label userLabel = new Label("User Logged In");
				   		   HorizontalPanel userRow = new HorizontalPanel();
				   		   userRow.add(userLabel);
				   		   userPanel.add(userRow);
				   		   userRow.add(logoutButton);
				   		   mainPanel.add(userPanel);
				   	   }
				   	   else {
				   		   mainPanel.clear();
				   		   VerticalPanel addLoginPanel = new VerticalPanel();
				   		   Label userLabel = new Label("User Name");
				   		   HorizontalPanel userRow = new HorizontalPanel();
				   		   userRow.add(userLabel);
				   		   userRow.add(userBox);
				   		   addLoginPanel.add(userRow);
				   		   Label passLabel = new Label("Password");
				   		   HorizontalPanel passRow = new HorizontalPanel();
				   		   passRow.add(passLabel);
				   		   passRow.add(passBox);
				   		   addLoginPanel.add(passRow);
				   		   addLoginPanel.add(loginButton);
				   	   	   Frame frame = new Frame("http://localhost:3000/questions/chooseSurvey");
				   		   frame.setWidth("100%");
				   		   frame.setHeight("450px");
				   		   mainPanel.add(addLoginPanel);
				   		   mainPanel.add(frame);
				   	   }
				   }
				   else if (postType.equals("View Results")) {
					   
				   }
				   else if (postType.equals("getSurveyListBox")) {
					   String[] survey_names = response.getText().split(",");
					   surveyListBox = new ListBox();
					   for (String sname : survey_names) {
						   surveyListBox.addItem(sname);
					   }
					   surveyListBox.setVisibleItemCount(survey_names.length);
					   mainPanel.add(surveyListBox);
					   setupViewSurvey();
				   }
			   }
		   });
	   }
	   catch (Exception e) {
		   Window.alert(e.getMessage());
	   }
   } // end postRequest()
   private void showUsers(String responseText)
   {
	  jsonData = getData(responseText);
	  users = new ArrayList<MyUser>();
      User user = null;
      for (int i = 0; i < jsonData.length(); i++) {
         user = jsonData.get(i);
         users.add(new MyUser(user.getID(),
            user.getFirstName(), user.getLastName(),user.getUserName(),user.getDepartment(),
            user.getDivsion()));
      }
      CellTable<MyUser> table = new CellTable<MyUser>();
      TextColumn<MyUser> fnameCol = 
         new TextColumn<MyUser>()
         {
            @Override
            public String getValue(MyUser user)
            {
               return user.first_name;
            }
         };
      TextColumn<MyUser> lnameCol = 
         new TextColumn<MyUser>()
         {
            @Override
            public String getValue(MyUser user)
            {
               return user.last_name;
            }
         };
      TextColumn<MyUser> userNameCol =
    	  new TextColumn<MyUser>()
    	  {
    	  	@Override
    	  	public String getValue(MyUser user)
    	  	{
    	  		return user.username;
    	  	}
    	  };
      TextColumn<MyUser> departmentCol =
        	  new TextColumn<MyUser>()
        	  {
        	  	@Override
        	  	public String getValue(MyUser user)
        	  	{
        	  		return user.department;
        	  	}
        	  };
      TextColumn<MyUser> divisionCol =
        	  new TextColumn<MyUser>()
        	  {
        	  	@Override
        	  	public String getValue(MyUser user)
        	  	{
        	  		return user.division;
        	  	}
        	  };
            	  
    	  
    	  /*
      final SingleSelectionModel<MyUser> selectionModel =
    	 new SingleSelectionModel<MyUser>();
      table.setSelectionModel(selectionModel);
      selectionModel.addSelectionChangeHandler(
    	 new SelectionChangeEvent.Handler()
    	 {
    		 public void onSelectionChange(SelectionChangeEvent e)
    		 {
    			 MyUser selected = selectionModel.getSelectedObject();
    			 if (selected != null) {
    				 selectedStudent = selected;
    			 }
    		 }
    	 });
    	 */
      table.addColumn(fnameCol, "First Name");
      table.addColumn(lnameCol, "Last Name");
      table.addColumn(userNameCol, "UserName");
      table.addColumn(departmentCol, "Department");
      table.addColumn(divisionCol, "Division");
      table.setRowCount(users.size(),true);
      table.setRowData(0,users);
      mainPanel.add(table);
   } 
   private void setupLogin()
   {
	   mainPanel.clear();
	   VerticalPanel addLoginPanel = new VerticalPanel();
	   Label userLabel = new Label("User Name");
	   HorizontalPanel userRow = new HorizontalPanel();
	   userRow.add(userLabel);
	   userRow.add(userBox);
	   addLoginPanel.add(userRow);
	   Label passLabel = new Label("Password");
	   HorizontalPanel passRow = new HorizontalPanel();
	   passRow.add(passLabel);
	   passRow.add(passBox);
	   addLoginPanel.add(passRow);
	   addLoginPanel.add(loginButton);
	   mainPanel.add(addLoginPanel);  
   }
   private void loginPanel()
   {
	   mainPanel.clear();
	   VerticalPanel newLoginPanel = new VerticalPanel();
	   Label loginLabel = new Label("Still logged in");
	   HorizontalPanel loginRow = new HorizontalPanel();
	   loginRow.add(loginLabel);
	   newLoginPanel.add(loginRow);
	   loginRow.add(addButton);
	   loginRow.add(viewResults);
	   loginRow.add(logoutButton);
	   mainPanel.add(newLoginPanel);  
   }
   private void setupViewSurvey()
   {
	  VerticalPanel viewSurvey = new VerticalPanel();
	  ((ForIsWidget) viewResults).add(chooseResults);
	  mainPanel.add(viewResults);
   }
   private JsArray<User> getData(String json)
   {
      return JsonUtils.safeEval(json);
   }
   public void setWidth(String width)
   {
	   String fwidth = "1000px";
	   fwidth = width;
   }   
   public void setHeight(String height)
   {
	   String fheight = "1000px";
	   fheight = height;   
   }
}
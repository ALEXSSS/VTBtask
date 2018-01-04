<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <title>AngularJS $http Example</title>  
    <style>
      .num.ng-valid {
          background-color: lightgreen;
      }
      .num.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .num.ng-dirty.ng-invalid-minlength {
          background-color: yellow;
      }

      .date.ng-valid {
          background-color: lightgreen;
      }
      .date.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .date.ng-dirty.ng-invalid-email {
          background-color: yellow;
      }

    </style>
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
  </head>
  <body ng-app="myApp" class="ng-cloak">
      <div class="generic-container" ng-controller="EntryController as ctrl">

          <input type="file" id="file" name="file" accept=".xls,.xlsx" enctype="multipart/form-data" />
          <button ng-click="ctrl.add()">Add</button>

          <button type="button" ng-click="ctrl.show()" class="btn btn-success custom-width">Show</button>


          <div class="panel panel-default">
              <div class="panel-heading"><span class="lead">Entry Registration Form </span></div>
              <div class="formcontainer">
                  <form ng-submit="ctrl.submitEntry()" name="myForm" class="form-horizontal">
                      <input type="hidden" ng-model="ctrl.entry.id" />
                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">Num</label>
                              <div class="col-md-7">
                                  <input type="text" ng-model="ctrl.entry.num" name="num" class="num form-control input-sm" placeholder="Enter your num" ng-pattern="/^\d*$/" required ng-minlength="1" ng-maxlength="18" />
                                  <div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.num.$error.required">This is a required field</span>
                                      <span ng-show="myForm.num.$error.pattern">Write a number</span>
                                      <span ng-show="myForm.num.$error.maxlength">Maximum length required is 18</span>
                                      <span ng-show="myForm.num.$error.minlength">Minimum length required is 1</span>
                                  </div>
                              </div>
                          </div>
                      </div>


                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">string</label>
                              <div class="col-md-7">
                                  <input type="text" ng-model="ctrl.entry.string" class="form-control input-sm" placeholder="Enter your string."/>
                              </div>
                          </div>
                      </div>

                      <div class="row">
                          <div class="form-group col-md-12">
                              <label class="col-md-2 control-lable" for="file">date</label>
                              <div class="col-md-7">
                                  <input type="text" ng-model="ctrl.entry.date1" name="date" class="date form-control input-sm" placeholder="Enter your date" ng-pattern="/^([0-2]\d|30|31).(0\d|10|11|12).{1}\d{4}\s([0-1]\d|20|21|22|23|24).{1}(0\d|[1-5]\d).{1}(0\d|[1-5]\d)$/" required/>
                                  <div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.date.$error.required">This is a required field</span>
                                      <span ng-show="myForm.date.$error.pattern">format of string should be dd.mm.yyyy hh.mm.ss</span>
                                  </div>
                              </div>
                          </div>
                      </div>

                      <div class="row">
                          <div class="form-actions floatRight">
                              <input type="submit"  value="{{!ctrl.entry.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid">
                              <button type="button" ng-click="ctrl.resetEntry()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
                          </div>
                      </div>
                  </form>
              </div>
          </div>





          <div class="panel panel-default">
              <!-- Default panel contents -->
              <div class="panel-heading"><span class="lead">List of Entries</span></div>
              <div class="tablecontainer">
                  <table class="table table-hover">
                      <thead>
                      <tr>
                          <th>ID.</th>
                          <th>Num</th>
                          <th>String</th>
                          <th>Date</th>
                          <th width="20%"></th>
                      </tr>
                      </thead>
                      <tbody>
                      <tr ng-repeat="u in ctrl.entries">
                          <td><span ng-bind="u.id"></span></td>
                          <td><span ng-bind="u.num"></span></td>
                          <td><span ng-bind="u.string"></span></td>
                          <td><span ng-bind="u.date1"></span></td>
                          <td>
                              <button type="button" ng-click="ctrl.editEntry(u.id)" class="btn btn-success custom-width">Edit</button>  <button type="button" ng-click="ctrl.removeEntry(u.id)" class="btn btn-danger custom-width">Remove</button>
                          </td>
                      </tr>
                      </tbody>
                  </table>
              </div>


          </div>
      </div>
      
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      <script src="<c:url value='/static/js/service/entry_service.js' />"></script>
      <script src="<c:url value='/static/js/controller/entry_controller.js' />"></script>
  </body>
</html>
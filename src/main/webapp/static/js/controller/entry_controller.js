'use strict';

angular.module('myApp').controller('EntryController',  ['$scope','$http', 'EntryService', function($scope,$http, EntryService) {
    var self = this;
    self.user={id:null,username:'',address:'',email:''};
    self.entry={id:null,num:'',string:'',date1:''};
    self.users=[];
    self.entries=[];

    self.submitEntry = submitEntry;
    self.editEntry = editEntry;
    self.removeEntry = removeEntry;
    self.resetEntry = resetEntry;
    self.add = functionAdd;
    self.show = fetchAllEntries;

    var REST_SERVICE_URI1 = 'http://localhost:8080/VTB/file/';


    fetchAllEntries();



    function fetchAllEntries(){
        EntryService.fetchAllEntries()
            .then(
                function(d) {
                    self.entries = d;
                },
                function(errResponse){
                    console.error('Error while fetching Entries');
                }
            );
    }



    function createEntry(entry){
        EntryService.createEntry(entry)
            .then(
                fetchAllEntries,
                function(errResponse){
                    console.error('Error while creating Entry');
                }
            );
    }





    function updateEntry(entry, id){
        EntryService.updateEntry(entry, id)
            .then(
                fetchAllEntries,
                function(errResponse){
                    console.error('Error while updating Entry');
                }
            );
    }



    function deleteEntry(id){
        EntryService.deleteEntry(id)
            .then(
                fetchAllEntries,
                function(errResponse){
                    console.error('Error while deleting Entry');
                }
            );
    }




    function submitEntry() {
        if(self.entry.id===null){
            console.log('Saving New Entry', self.entry);
            createEntry(self.entry);
        }else{
            updateEntry(self.entry, self.entry.id);
            console.log('Entry updated with id ', self.entry.id);
        }
        resetEntry();
    }



    function editEntry(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.entries.length; i++){
            if(self.entries[i].id === id) {
                self.entry = angular.copy(self.entries[i]);
                break;
            }
        }
    }



    function removeEntry(id){
        console.log('id to be deleted', id);
        if(self.entry.id === id) {//clean form if the user to be deleted is shown there.
            resetEntry();
        }
        deleteEntry(id);
    }



    function resetEntry(){
        self.entry={id:null,num:'',string:'',date:''};
        $scope.myForm.$setPristine(); //reset Form
    }

    function functionAdd() {
        var f = document.getElementById('file').files[0];
        var fd = new FormData();

        fd.append("file", f);
        var formData=new FormData();
        formData.append("file",f);
        $http.post(REST_SERVICE_URI1, formData, {
            transformRequest: function(data, headersGetterFunction) {
                return data;
            },
            headers: { 'Content-Type': undefined }
        }).success(function(document) {
            console.log(document)
            alert(document.message);
        })
    }

}]);

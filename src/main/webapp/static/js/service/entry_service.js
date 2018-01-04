'use strict';

angular.module('myApp').factory('EntryService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI_E = 'http://localhost:8080/VTB/entry/';
    var REST_SERVICE_URI1 = 'http://localhost:8080/VTB/file/';

    var factory = {
        fetchAllEntries: fetchAllEntries,
        createEntry: createEntry,
        updateEntry:updateEntry,
        deleteEntry:deleteEntry,
    }

    return factory;



    function fetchAllEntries() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI_E)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while fetching Entries');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function createEntry(entry) {
        var deferred = $q.defer();
        console.log("createEntry : ", entry);
        $http.post(REST_SERVICE_URI_E, entry)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating Entry');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }




    function updateEntry(entry, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI_E+id, entry)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while updating Entry');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }



    function deleteEntry(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI_E+id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while deleting Entry');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);

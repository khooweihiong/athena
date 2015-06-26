'use strict';

angular.module('athenaApp')
    .factory('Borrower', function ($resource, DateUtils) {
        return $resource('api/borrowers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.takenDate = DateUtils.convertDateTimeFromServer(data.takenDate);
                    data.returnDate = DateUtils.convertDateTimeFromServer(data.returnDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

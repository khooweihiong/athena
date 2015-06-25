'use strict';

angular.module('athenaApp')
    .factory('Book', function ($resource, DateUtils) {
        return $resource('api/books/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

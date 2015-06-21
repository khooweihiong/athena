'use strict';

angular.module('athenaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });



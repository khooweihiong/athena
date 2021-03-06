'use strict';

angular.module('athenaApp')
    .controller('BookDetailController', function ($scope, $stateParams, Book, User) {
        $scope.book = {};
        $scope.load = function (id) {
            Book.get({id: id}, function(result) {
              $scope.book = result;
            });
        };
        $scope.load($stateParams.id);
    });

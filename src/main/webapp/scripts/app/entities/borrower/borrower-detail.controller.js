'use strict';

angular.module('athenaApp')
    .controller('BorrowerDetailController', function ($scope, $stateParams, Borrower, User, Book) {
        $scope.borrower = {};
        $scope.load = function (id) {
            Borrower.get({id: id}, function(result) {
              $scope.borrower = result;
            });
        };
        $scope.load($stateParams.id);
    });

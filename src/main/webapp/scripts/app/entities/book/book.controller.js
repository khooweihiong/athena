'use strict';

angular.module('athenaApp')
    .controller('BookController', function ($scope, Book, User, ParseLinks) {
        $scope.books = [];
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Book.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.books = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Book.get({id: id}, function(result) {
                $scope.book = result;
                $('#saveBookModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.book.id != null) {
                Book.update($scope.book,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Book.save($scope.book,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Book.get({id: id}, function(result) {
                $scope.book = result;
                $('#deleteBookConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Book.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBookConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBookModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.book = {title: null, author: null, description: null, publisher: null, available: null, created: null, updated: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

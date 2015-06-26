'use strict';

angular.module('athenaApp')
    .controller('BorrowerController', function ($scope, Borrower, User, Book, ParseLinks) {
        $scope.borrowers = [];
        $scope.users = User.query();
        $scope.books = Book.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Borrower.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.borrowers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Borrower.get({id: id}, function(result) {
                $scope.borrower = result;
                $('#saveBorrowerModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.borrower.id != null) {
                Borrower.update($scope.borrower,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Borrower.save($scope.borrower,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Borrower.get({id: id}, function(result) {
                $scope.borrower = result;
                $('#deleteBorrowerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Borrower.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBorrowerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBorrowerModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.borrower = {takenDate: null, returnDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });

'use strict';

angular.module('athenaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('borrower', {
                parent: 'entity',
                url: '/borrower',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Borrowers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/borrower/borrowers.html',
                        controller: 'BorrowerController'
                    }
                },
                resolve: {
                }
            })
            .state('borrowerDetail', {
                parent: 'entity',
                url: '/borrower/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'Borrower'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/borrower/borrower-detail.html',
                        controller: 'BorrowerDetailController'
                    }
                },
                resolve: {
                }
            });
    });

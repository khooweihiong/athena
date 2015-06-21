'use strict';

angular.module('athenaApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });

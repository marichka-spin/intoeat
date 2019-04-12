app.controller('nav-ctrl', ['$scope', '$location', function($scope, $location){

    var NavItem = function(name, path) {
        this.name = name,
        this.path = path;
    };

    $scope.items = [
        new NavItem('Places', '#/places'),
        new NavItem('Tags', '#/tags'),
        new NavItem('Tags Groups', '#/groups')];

    $scope.isActive = function(path) {
        return ('#' + $location.path()).indexOf(path) > -1;
    };

}]);
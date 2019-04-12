app.controller('groups-ctrl', ['$scope', 'Group', function($scope, Group){

    $scope.addNew = '#/groups/0';

    var loadGroups = function() {
        Group.query({path : 'all'}, function(data){
            $scope.groups = data;
        });
    };

    loadGroups();

    $scope.searchGroups = function() {
        if ($scope.searchArg)  {
            Group.query(
                {path : 'search', arg : $scope.searchArg}, function(data){
                    $scope.groups = data;
                });
        }
    };

    $scope.searchGroupsOnEnter = function(e) {
        if (e.which === 13) {
            $scope.searchGroups();
        }
    };

    $scope.removeGroup = function(id, index){
        Group.delete({path : 'remove', id: id}, function(){
            $scope.groups.splice(index, 1);
        });
    };

    $scope.buildGroupUrl = function(id){
        return '#/groups/' + id;
    }

}]);
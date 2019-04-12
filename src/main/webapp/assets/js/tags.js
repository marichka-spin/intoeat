app.controller('tags-ctrl', ['$scope', 'Tag', function($scope, Tag){

    $scope.addNew = '#/tags/0';

    var loadTags = function() {
        Tag.query({path : 'all'}, function(data){
            $scope.tags = data;
        });
    };

    loadTags();

    $scope.searchTags = function() {
        if ($scope.searchArg)  {
            Tag.query(
                {path : 'search', arg : $scope.searchArg}, function(data){
                    $scope.tags = data;
                });
        }
    };

    $scope.searchTagsOnEnter = function(e) {
        if (e.which === 13) {
            $scope.searchTags();
        }
    };

    $scope.removeTag = function(id, index){
        Tag.delete({path : 'remove', id: id}, function(){
            $scope.tags.splice(index, 1);
        });
    };

    $scope.buildTagUrl = function(id){
        return '#/tags/' + id;
    }

}]);
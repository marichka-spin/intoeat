app.controller('places-ctrl', ['$scope', 'Place', function($scope, Place){

    $scope.addNew = '#/places/0';

    var loadPlaces = function() {
        Place.query({path : 'all'}, function(data){
            $scope.places = data;
        });
    };

    loadPlaces();

    $scope.searchPlaces = function() {
        if ($scope.searchArg)  {
            Place.query(
                {path : 'search', arg : $scope.searchArg}, function(data){
                    $scope.places = data;
                });
        }
    };

    $scope.searchPlacesOnEnter = function(e) {
        if (e.which === 13) {
            $scope.searchPlaces();
        }
    };

    $scope.removePlace = function(id, index){
        Place.delete({path : 'remove', id: id}, function(){
            $scope.places.splice(index, 1);
        });
    };

    $scope.buildPlaceUrl = function(id){
        return '#/places/' + id;
    }

}]);
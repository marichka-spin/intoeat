app.controller('tag-ctrl',
    ['$scope', '$routeParams', '$location', 'Tag', function($scope, $routeParams, $location, Tag){

        $scope.listUrl = '#/tags';

        $scope.id = $routeParams.tagId;

        var TagModel = function() {
            var self = this;

            self.id = 0;
            self.name = '';
            self.description = '';

            self.populateData = function(data) {
                self.id = data.id;
                self.name = data.name;
                self.description = data.description;
            }
        };

        var loadTag  = function(){
            if ($scope.id > 0) {
                Tag.get({id : $scope.id}, function(data){
                    $scope.tag = new TagModel();
                    $scope.tag.populateData(data);
                });
            } else {
                $scope.tag = new TagModel();
            }
        };

        loadTag();

        $scope.saveTag = function() {
            Tag.save({path: 'save'}, $scope.tag, function(){
                $location.path('/tags');
            });
        };

    }]);

app.controller('group-ctrl',
    ['$scope', '$routeParams', '$location', 'Group', 'Tag',
        function($scope, $routeParams, $location, Group, Tag){

        $scope.listUrl = '#/groups';
        $scope.id = $routeParams.groupId;
        $scope.allTags = {};
        $scope.availableTags = [];

        var GroupModel = function() {
            var self = this;

            self.id = 0;
            self.name = '';
            self.description = '';
            self.tagsIds = [];

            self.populateData = function(data) {
                self.id = data.id;
                self.name = data.name;
                self.description = data.description;
            };

            self.populateTags = function(data) {
                var tagsIds = data.tagsIds;
                for (var key in $scope.allTags) {
                    var tag = $scope.allTags[key];
                    if (tagsIds.indexOf(parseInt(key)) > -1) {
                        self.tagsIds.push(tag.id);
                    } else {
                        tag.available = true;
                        $scope.availableTags.push(tag.id);
                    }
                }
            };
        };

        var TagModel = function(id, name) {
            var self = this;

            self.id = id;
            self.name = name;
            self.available = false;
            self.selected = false;

            self.selectTag = function() {
                self.selected = !self.selected;
            };
        };

        var loadGroup  = function(){
            if ($scope.id > 0) {
                Group.get({id : $scope.id}, function(data){
                    var groupData = data;
                    $scope.group = new GroupModel();
                    $scope.group.populateData(groupData);
                    Tag.query({path : 'all'}, function(data){
                        for (var i = 0; i < data.length; i++) {
                            var tag = data[i];
                            $scope.allTags[tag.id] = new TagModel(tag.id, tag.name);
                        }
                        $scope.group.populateTags(groupData);
                    });
                });
            } else {
                $scope.group = new GroupModel();
                Tag.query({path : 'all'}, function(data){
                    for (var i = 0; i < data.length; i++) {
                        var tag = data[i];
                        $scope.allTags[tag.id] = new TagModel(tag.id, tag.name);
                        $scope.availableTags.push(tag.id);
                    }
                });
            }
        };

        loadGroup();

        $scope.addTags = function() {
            var tagsToAdd = [];
            var availableTags = [];
            for (var i = 0; i < $scope.availableTags.length; i++) {
                var tagId = $scope.availableTags[i];
                var tag = $scope.allTags[tagId];
                if (tag.selected) {
                    tag.selected = !tag.selected;
                    tagsToAdd.push(tagId);
                } else {
                    availableTags.push(tagId)
                }
            }
            $scope.availableTags = availableTags;
            $scope.group.tagsIds =
                $scope.group.tagsIds.concat(tagsToAdd);
        };

        $scope.removeTags = function() {
            var tagsToRemove = [];
            var selectedTags = [];
            for (var i = 0; i < $scope.group.tagsIds.length; i++) {
                var tagId = $scope.group.tagsIds[i];
                var tag = $scope.allTags[tagId];
                if (tag.selected) {
                    tag.selected = !tag.selected;
                    tagsToRemove.push(tagId);
                } else {
                    selectedTags.push(tagId)
                }
            }
            $scope.availableTags =
                $scope.availableTags.concat(tagsToRemove);
            $scope.group.tagsIds = selectedTags;
        };

        $scope.saveGroup = function() {
            Group.save({path: 'save'}, $scope.group, function(){
                $location.path('/groups');
            });
        };

    }]);

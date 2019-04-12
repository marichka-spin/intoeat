app.controller('place-ctrl',
    ['$scope', '$routeParams', '$location', 'Place', 'Tag',
        function($scope, $routeParams, $location, Place, Tag){

        $scope.listUrl = '#/places';
        $scope.id = $routeParams.placeId;
        $scope.allTags = {};
        $scope.availableTags = [];

        var PlaceModel = function() {
            var self = this;

            self.id = 0;
            self.name = '';
            self.image = '';
            self.description = '';
            self.baseDir = '';
            self.tagsIds = [];
            self.contacts = [];

            self.populateData = function(data) {
                self.id = data.id;
                self.name = data.name;
                self.image = data.image;
                self.description = data.description;
                self.baseDir = data.baseDir;
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

            self.populateContacts = function(data) {
                var contacts = data.contacts;
                for (var i = 0; i < contacts.length; i++) {
                    var contact = new ContactModel();
                    contact.populateData(contacts[i]);
                    self.contacts.push(contact);
                }
            };

            self.toJSON = function() {
                var _contacts = [];
                for (var i = 0; i < self.contacts.length; i++) {
                    var _contact = self.contacts[i].toJSON();
                    _contacts.push(_contact);
                }
                return {
                    "id" : self.id,
                    "name" : self.name,
                    "image" : self.image,
                    "description" : self.description,
                    "tagsIds" : self.tagsIds,
                    "contacts" : _contacts
                };
            }
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

        var ContactModel = function() {
            var self = this;

            self.id = 0;
            self.address = '';
            self.email = '';
            self.phone = '';
            self.webSite = '';
            self.workTime = '';

            self.collapsed = true;

            self.populateData = function(data) {
                self.id = data.id;
                self.address = data.address;
                self.email = data.email;
                self.phone = data.phone;
                self.webSite = data.webSite;
                self.workTime = data.workTime;
            };

            self.toJSON = function() {
                return {
                    "id" : self.id,
                    "address" : self.address,
                    "email" : self.email,
                    "phone" : self.phone,
                    "webSite" : self.webSite,
                    "workTime" : self.workTime
                };
            }
        };

        var loadPlace  = function(){
            if ($scope.id > 0) {
                Place.get({id : $scope.id}, function(data){
                    var placeData = data;
                    $scope.place = new PlaceModel();
                    $scope.place.populateData(placeData);
                    $scope.place.populateContacts(placeData);
                    Tag.query({path : 'all'}, function(data){
                        for (var i = 0; i < data.length; i++) {
                            var tag = data[i];
                            $scope.allTags[tag.id] = new TagModel(tag.id, tag.name);
                        }
                        $scope.place.populateTags(placeData);
                    });
                });
            } else {
                $scope.place = new PlaceModel();
                Tag.query({path : 'all'}, function(data){
                    for (var i = 0; i < data.length; i++) {
                        var tag = data[i];
                        $scope.allTags[tag.id] = new TagModel(tag.id, tag.name);
                        $scope.availableTags.push(tag.id);
                    }
                });
            }
        };

        loadPlace();

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
            $scope.place.tagsIds =
                $scope.place.tagsIds.concat(tagsToAdd);
        };

        $scope.removeTags = function() {
            var tagsToRemove = [];
            var selectedTags = [];
            for (var i = 0; i < $scope.place.tagsIds.length; i++) {
                var tagId = $scope.place.tagsIds[i];
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
            $scope.place.tagsIds = selectedTags;
        };

        $scope.addContact = function() {
            var contact = new ContactModel();
            contact.collapsed = false;
            $scope.place.contacts.unshift(contact);
        };

        $scope.removeContact = function(index) {
            $scope.place.contacts.splice(index, 1);
        };

        $scope.savePlace = function() {
            Place.save({path: 'save'}, $scope.place.toJSON(), function(){
                $location.path('/places');
            });
        };

        $scope.displayAddress = function(index) {
            var $container = $('#contact' + index);
            if($container.is(':hidden')) {
                $container.slideDown('slow');
            } else {
                $container.slideUp('slow');
            }
        };

        $scope.completeUpload = function() {
            var val = $("#p-logo .form-control").val();
            var index =  val.lastIndexOf('\\');
            var fileName = val.substring(index);
            console.log(fileName);
            $scope.place.image = fileName;
        }

    }]);
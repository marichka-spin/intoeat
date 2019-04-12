<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html ng-app="intoeat-app">
    <head>
        <title>InToEat</title>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="<c:url value='assets/css/bootstrap.css'/>"/>
        <link rel="stylesheet" href="<c:url value='assets/css/bootstrap.css.map'/>"/>
        <link rel="stylesheet" href="<c:url value='assets/css/app.css'/>"/>
        <link rel="stylesheet" href="<c:url value='assets/css/multi-select.css'/>"/>
    </head>
    <body>
        <div class="container">
            <nav class="navbar navbar-inverse" ng-controller="nav-ctrl">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="#/">InToEat Admin</a>
                    </div>
                    <div>
                        <ul class="nav navbar-nav">
                            <li ng-repeat="item in items" ng-class="{active:isActive(item.path)}">
                                <a href="{{ item.path }}" ng-bind="item.name"></a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
            <ng-view></ng-view>
        </div>
        <script type="text/javascript" src="<c:url value='assets/js/jquery.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/angular.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/angular-route.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/angular-resource.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/ng-upload.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/bootstrap.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/app.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/nav.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/places.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/place.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/tags.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/tag.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/groups.js'/>"></script>
        <script type="text/javascript" src="<c:url value='assets/js/group.js'/>"></script>
    </body>
</html>

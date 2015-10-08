'use strict';

/**
 * The root zachariliusApp module.
 * 
 * @type {zachariliusApp|*|{}}
 */
var zachariliusApp = zachariliusApp || {};

/**
 * @ngdoc module
 * @name odd-jobControllers
 * 
 * @description Angular module for controllers.
 * 
 */
zachariliusApp.controllers = angular.module('portfolioControllers', [ 'ui.bootstrap' ]);

zachariliusApp.controllers.controller('ContactCtrl', 
		function ($scope, HTTP_ERRORS) {
	$scope.loading = false;
	$scope.email = $scope.email || {};

    $scope.isValidEmailForm = function (emailForm) {
        return emailForm.$valid;
    }	
	
	$scope.sendEmail = function(emailForm){
		console.log(emailForm);
		console.log($scope.email);
        if (!$scope.isValidEmailForm(emailForm)) {
            return;
        }		
		
		$scope.loading = true;
		gapi.client.portfolio.sendEmail($scope.email).
            execute(function (resp) {
                $scope.$apply(function () {
                	if(resp.error){
                		//Error when sending email
                        var errorMessage = resp.error.message || '';
                        $scope.messages = 'Failed to send the email : ' + errorMessage;
                        $scope.alertStatus = 'warning';
                    } else{
                    	console.log(resp);
                		//Email send successfully
                    	$scope.messages = "Email sent successfully";
                    	$scope.alertStatus = "success";
                        $scope.emailForm = {};
                		
                	}
                    $scope.loading = false;
                    $scope.email = {};
                });
			});
		}
});

zachariliusApp.controllers.controller('MapCtrl', function($scope){
	$scope.onMouseover = function(event) {
	    var fillArray = ['red', 'blue', 'yellow', 'green'];
	    var style = this.getFeatureStyle(event.featureId); 
	    style.fillColor = fillArray[event.featureId - 1];
	    style.fillOpacity = '0.8';
	  };
	  $scope.onMouseout = function(event) {
	    var style = this.getFeatureStyle(event.featureId).resetAll();
	  };
});

zachariliusApp.controllers.controller('ProjectsCtrl', function($scope){
	$scope.p1isActive = "desktop";
	$scope.p2isActive = "desktop";

	$scope.p1image = '/images/project/odd-job_DesktopView.png';
	$scope.p2image = '/images/project/odd-job_DesktopView.png';
	
	$scope.isActive = function(project, type){
		if(project === 'p1'){
			if(type === $scope.p1isActive){
				console.log("active: " + project + type)
				return 'active';
			};
		}
		else if(project === 'p2'){
			if(type === $scope.p2isActive){
				return 'active';
			};
		};
	};
	
	$scope.setImage = function(project, type){
		if(project === 'p1'){
			if(type === 'desktop'){
				$scope.p1image = '/images/project/odd-job_DesktopView.png';
				$scope.p1isActive = "desktop";
			}
			else if(type === 'tablet'){
				$scope.p1image = '/images/project/odd-job_TabletView.png';
				$scope.p1isActive = "tablet";
			}	
			else if(type === 'mobile'){
				$scope.p1image = '/images/project/odd-job_MobileView.png';
				$scope.p1isActive = "mobile";
			}		
		}
		else if(project === 'p2'){
			if(type === 'desktop'){
				$scope.p2image = '/images/project/odd-job_DesktopView.png';
				$scope.p2isActive = "desktop";
			}
			else if(type === 'tablet'){
				$scope.p2image = '/images/project/odd-job_TabletView.png';
				$scope.p2isActive = "tablet";
			}	
			else if(type === 'mobile'){
				$scope.p2image = '/images/project/odd-job_MobileView.png';
				$scope.p2isActive = "mobile";
			}		
		};	
	};
});



/**
 * @ngdoc controller
 * @name MyProfileCtrl
 * 
 * @description A controller used for the My Profile page.
 */
zachariliusApp.controllers.controller('ProfileCtrl', function($scope, $log,
		oauth2Provider, HTTP_ERRORS, $rootScope) {
	
	$scope.receivedJobs = false;
	$scope.loading = false;
	$scope.selectedTab == 'MAP';
	
	/*
	 * The initial profile retrieved from the server to know the dirty state.
	 * 
	 * @type {{}}
	 */
	
	$scope.profile = {};
	
	$scope.initialProfile = {};

	$scope.initialJobs = {};
	
	/**
	 * Sets the selected tab to 'MAP'
	 */
	$scope.SearchByMap = function() {
		$scope.selectedTab = 'MAP';
	};

	/**
	 * Sets the selected tab to 'LIST'
	 */
	$scope.SearchByList = function() {
		$scope.selectedTab = 'LIST';
	};
	
	$scope.formNotReady = function(){
	}
	/**
	 * Initializes the My profile page. Update the profile if the user's profile
	 * has been stored.
	 */
	$scope.init = function() {
		var retrieveProfileCallback = function() {
			$scope.profile = {};
			$scope.loading = true;
			gapi.client.oddjob.getProfile().execute(function(resp) {
				$scope.$apply(function() {
					$scope.loading = false;
					if (resp.error) {
						// Failed to get a user profile.
					} else {
						// Succeeded to get the user profile.
						console.log(resp.result);
						$scope.profile.displayName = resp.result.displayName;
						$scope.profile.city = resp.result.city;
						$scope.profile.state = resp.result.state;
						$scope.profile.phoneNumber = resp.result.phoneNumber;	
						$scope.profile.pictureUrl = resp.result.pictureUrl;
						console.log("$scope.profile");
						console.log($scope.profile);
						
						$scope.initialProfile = resp.result;
					}
				});
			});
		};

		if (!oauth2Provider.signedIn) {
			var modalInstance = oauth2Provider.showLoginModal();
			modalInstance.result.then(retrieveProfileCallback).then(
					$scope.retrievePostedJobs);
		} else {
			retrieveProfileCallback();
			$scope.retrievePostedJobs();
		}
	};

	$scope.retrievePostedJobs = function() {
		$scope.jobs = [];
		$scope.loading = true;
		gapi.client.oddjob.getJobsCreated().execute(function(resp) {
			$scope.$apply(function() {
				$scope.loading = false;
				$scope.receivedJobs = true;
				if (resp.error) {
					console.log("ERROR retrieving my jobs");
				} else {
					angular.forEach(resp.items, function(job) {
						$scope.jobs.push(angular.copy(job));
					});
					console.log(resp);
					$scope.initialJobs = resp.items.slice();
				}
			});

		})
	};
	/**
	 * Invokes the oddjob.saveProfile API.
	 * 
	 */
	$scope.saveProfile = function() {
		$scope.loading = true;
		$scope.profile.pictureUrl = $scope.$root.profilePicture;
		gapi.client.oddjob.saveProfile($scope.profile).execute(
				function(resp) {
					$scope.$apply(function() {
						$scope.loading = false;
						if (resp.error) {
							// The request has failed.
							var errorMessage = resp.error.message || '';
							$scope.messages = 'Failed to update a profile : '
									+ errorMessage;
							$scope.alertStatus = 'warning';
							$log.error($scope.messages + 'Profile : '
									+ JSON.stringify($scope.profile));

							if (resp.code
									&& resp.code == HTTP_ERRORS.UNAUTHORIZED) {
								oauth2Provider.showLoginModal();
								return;
							}
						} else {
							// The request has succeeded.
							$scope.messages = 'The profile has been updated';
							$scope.alertStatus = 'success';
							$scope.submitted = false;
							$scope.initialProfile = {
									displayName: $scope.profile.displayName,
									city:  resp.result.city,
									state: resp.result.state,
									phoneNumber: resp.result.phoneNumber
							};

							$log.info($scope.messages
									+ JSON.stringify(resp.result));
						}
					});
				});
	};
});

/**
 * @ngdoc controller
 * @name RootCtrl
 * 
 * @description The root controller having a scope of the body element and
 *              methods used in the application wide such as user
 *              authentications.
 * 
 */
zachariliusApp.controllers.controller('RootCtrl', function($scope, $location,
		oauth2Provider) {

	/**
	 * Returns if the viewLocation is the currently viewed page.
	 * 
	 * @param viewLocation
	 * @returns {boolean} true if viewLocation is the currently viewed page.
	 *          Returns false otherwise.
	 */
	$scope.isActive = function(viewLocation) {
		return viewLocation === $location.path();
	};

	/**
	 * Returns the OAuth2 signedIn state.
	 * 
	 * @returns {oauth2Provider.signedIn|*} true if siendIn, false otherwise.
	 */
	$scope.getSignedInState = function() {
		return oauth2Provider.signedIn;
	};

	/**
	 * Calls the OAuth2 authentication method.
	 */
	$scope.signIn = function() {
		oauth2Provider.signIn(function() {
			gapi.client.oauth2.userinfo.get().execute(function(resp) {
				$scope.$apply(function() {
					if (resp.email) {
						oauth2Provider.signedIn = true;
						$scope.alertStatus = 'success';
						$scope.rootMessages = 'Logged in with ' + resp.email;
						console.log(resp);
						$scope.$root.profilePicture = resp.picture;
					}
				});
			});
		});
	};

	/**
	 * Render the signInButton and restore the credential if it's stored in the
	 * cookie. (Just calling this to restore the credential from the stored
	 * cookie. So hiding the signInButton immediately after the rendering)
	 */
	$scope.initSignInButton = function() {
		gapi.signin.render('signInButton', {
			'callback' : function() {
				jQuery('#signInButton button').attr('disabled', 'true').css(
						'cursor', 'default');
				if (gapi.auth.getToken() && gapi.auth.getToken().access_token) {
					$scope.$apply(function() {
						oauth2Provider.signedIn = true;
					});
				}
			},
			'clientid' : oauth2Provider.CLIENT_ID,
			'cookiepolicy' : 'single_host_origin',
			'scope' : oauth2Provider.SCOPES
		});
	};

	/**
	 * Logs out the user.
	 */
	$scope.signOut = function() {
		oauth2Provider.signOut();
		$scope.alertStatus = 'success';
		$scope.rootMessages = 'Logged out';
	};

	/**
	 * Collapses the navbar on mobile devices.
	 */
	$scope.collapseNavbar = function() {
		angular.element(document.querySelector('.navbar-collapse'))
				.removeClass('in');
	};

});

/**
 * @ngdoc controller
 * @name OAuth2LoginModalCtrl
 * 
 * @description The controller for the modal dialog that is shown when an user
 *              needs to login to achive some functions.
 * 
 */
zachariliusApp.controllers.controller('OAuth2LoginModalCtrl', function($scope,
		$modalInstance, $rootScope, oauth2Provider) {
	$scope.singInViaModal = function() {
		oauth2Provider.signIn(function() {
			gapi.client.oauth2.userinfo.get().execute(function(resp) {
				$scope.$root.$apply(function() {
					oauth2Provider.signedIn = true;
					$scope.$root.alertStatus = 'success';
					$scope.$root.rootMessages = 'Logged in with ' + resp.email;
					$scope.$root.profilePicture = resp.picture;
					console.log($scope.$root.profilePicture);
				});

				$modalInstance.close();
			});
		});
	};
});

/**
 * @ngdoc controller
 * @name DatepickerCtrl
 * 
 * @description A controller that holds properties for a datepicker.
 */
zachariliusApp.controllers
		.controller('DatepickerCtrl',
				function($scope) {
					$scope.today = function() {
						$scope.dt = new Date();
					};
					$scope.today();

					$scope.clear = function() {
						$scope.dt = null;
					};

					// Disable weekend selection
					$scope.disabled = function(date, mode) {
						return (mode === 'day' && (date.getDay() === 0 || date
								.getDay() === 6));
					};

					$scope.toggleMin = function() {
						$scope.minDate = ($scope.minDate) ? null : new Date();
					};
					$scope.toggleMin();

					$scope.open = function($event) {
						$event.preventDefault();
						$event.stopPropagation();
						$scope.opened = true;
					};

					$scope.dateOptions = {
						'year-format' : "'yy'",
						'starting-day' : 1
					};

					$scope.formats = [ 'dd-MMMM-yyyy', 'yyyy/MM/dd',
							'shortDate' ];
					$scope.format = $scope.formats[0];
				});

/**
 * @ngdoc controller
 * @name JobCtrl
 * 
 * @description A controller used for the Post jobs page.
 */
zachariliusApp.controllers.controller('JobCtrl', function($scope, $log,
		oauth2Provider, HTTP_ERRORS, formInformation) {

	/**
	 * The oddjob object being edited in the page.
	 * 
	 * @type {{}|*}
	 */
	$scope.job = $scope.job || {};

	/**
	 * Holds the default values for the input candidates for city select.
	 * 
	 * @type {string[]}
	 */
	$scope.cities = formInformation.cities;

	$scope.states = formInformation.states;
	
	$scope.job.pay = 0;
	
	$scope.job.hours = 0;
	
	
	$scope.job.latitude = 47.6097;
	$scope.job.longitude = -122.3331;
	var mapCanvas = document.getElementById('map');
	var starterLatLng = {
		lat : $scope.job.latitude,
		lng : $scope.job.longitude
	};
	var mapOptions = {
		center : starterLatLng,
		zoom : 8,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	$scope.map = new google.maps.Map(mapCanvas, mapOptions);

	$scope.jobMarker = new google.maps.Marker({
		position : starterLatLng,
		map : $scope.map,
		title : 'Job',
		draggable : true
	});

	/**
	 * Holds the default values for the input candidates for tags select.
	 * 
	 * @type {string[]}
	 */
	$scope.tags = [ 'Construction', 'Moving Help', 'Web Development',
			'Sign Holding', 'Cleaning', 'Lawn Mowing', 'Pet Sitting',
			'Something Else' ];

	/**
	 * Tests if the oddjob.startDate and oddjob.endDate are valid.
	 * 
	 * @returns {boolean} true if the dates are valid, false otherwise.
	 */
	$scope.isValidDates = function() {
		var date = new Date();
		if (!$scope.job.completionDate) {
			return true;
		}
		return date.setHours(0, 0, 0, 0) <= $scope.job.completionDate;
	}

	/**
	 * Tests if $scope.oddjob is valid.
	 * 
	 * @param oddjobForm
	 *            the form object from the create_oddjobs.html page.
	 * @returns {boolean|*} true if valid, false otherwise.
	 */
	$scope.isValidJob = function(jobForm) {
		return !jobForm.$invalid && $scope.isValidDates();
	}

	/**
	 * Invokes the oddjob.createoddjob API.
	 * 
	 * @param oddjobForm
	 *            the form object.
	 */
	$scope.createJob = function(jobForm) {
		if (!$scope.isValidJob(jobForm)) {
			return;
		}
		$scope.job.latitude = $scope.jobMarker.position.H;
		$scope.job.longitude = $scope.jobMarker.position.L;
		$scope.loading = true;
		console.log($scope.job);
		gapi.client.oddjob.createJob($scope.job).execute(
				function(resp) {
					$scope.$apply(function() {
						$scope.loading = false;
						if (resp.error) {
							// The request has failed.
							var errorMessage = resp.error.message || '';
							$scope.messages = 'Failed to create a job : '
									+ errorMessage;
							$scope.alertStatus = 'warning';
							$log.error($scope.messages + ' Job : '
									+ JSON.stringify($scope.job));

							if (resp.code
									&& resp.code == HTTP_ERRORS.UNAUTHORIZED) {
								oauth2Provider.showLoginModal();
								return;
							}
						} else {
							// The request has succeeded.
							$scope.messages = 'The job has been created : '
									+ resp.result.title;
							$scope.alertStatus = 'success';
							$scope.submitted = false;
							$scope.job = {};
							$log.info($scope.messages + ' : '
									+ JSON.stringify(resp.result));
						}
					});
				});
	};
});
/**
 * @ngdoc controller
 * @name ShowJobCtrl
 * 
 * @description A controller used for the Show job page.
 */
zachariliusApp.controllers.controller('ShowJobCtrl', function($scope, $log,
		oauth2Provider, HTTP_ERRORS) {

	/**
	 * Holds the jobs displayed on the page
	 */
	$scope.jobs = [];

	/**
	 * Holds the status if the query is being executed.
	 * 
	 * @type {boolean}
	 */
	$scope.submitted = false;

	$scope.selectedTab = 'LIST';

	/**
	 * Map displaying all jobs
	 */
	$scope.markers = [];

	var latitude = 47.6097;
	var longitude = -122.3331;
	var mapCanvas = document.getElementById('map');
	var starterLatLng = {
		lat : latitude,
		lng : longitude
	};
	var mapOptions = {
		center : starterLatLng,
		zoom : 8,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var infoWindow = new google.maps.InfoWindow();

	$scope.map = new google.maps.Map(mapCanvas, mapOptions);
	$scope.markers = [];

	var createMarker = function(job) {

		var marker = new google.maps.Marker({
			map : $scope.map,
			position : new google.maps.LatLng(job.latitude, job.longitude),
			title : job.title
		});
		marker.content = '<div class="infoWindowContent">' + job.description
				+ '</div>';

		google.maps.event.addListener(marker, 'click', function() {
			infoWindow.setContent('<h2>' + job.title + '</h2>'
					+ job.description);
			infoWindow.open($scope.map, marker);
		});

		$scope.markers.push(marker);

	}
	$scope.openInfoWindow = function(e, selectedMarker) {
		e.preventDefault();
		google.maps.event.trigger(selectedMarker, 'click');
	}

	/**
	 * Holds the job currently displayed in the page.
	 * 
	 * @type {Array}
	 */
	$scope.job = [];

	/**
	 * Sets the selected tab to 'MAP'
	 */
	$scope.SearchByMap = function() {
		$scope.selectedTab = 'MAP';
	};

	/**
	 * Sets the selected tab to 'LIST'
	 */
	$scope.SearchByList = function() {
		$scope.selectedTab = 'LIST';
	};

	/**
	 * Namespace for the pagination.
	 * 
	 * @type {{}|*}
	 */
	$scope.pagination = $scope.pagination || {};
	$scope.pagination.currentPage = 0;
	$scope.pagination.pageSize = 20;
	/**
	 * Returns the number of the pages in the pagination.
	 * 
	 * @returns {number}
	 */
	$scope.pagination.numberOfPages = function() {
		return Math.ceil($scope.jobs.length / $scope.pagination.pageSize);
	};

	/**
	 * Returns an array including the numbers from 1 to the number of the pages.
	 * 
	 * @returns {Array}
	 */
	$scope.pagination.pageArray = function() {
		var pages = [];
		var numberOfPages = $scope.pagination.numberOfPages();
		for (var i = 0; i < numberOfPages; i++) {
			pages.push(i);
		}
		return pages;
	};

	/**
	 * Checks if the target element that invokes the click event has the
	 * "disabled" class.
	 * 
	 * @param event
	 *            the click event
	 * @returns {boolean} if the target element that has been clicked has the
	 *          "disabled" class.
	 */
	$scope.pagination.isDisabled = function(event) {
		return angular.element(event.target).hasClass('disabled');
	}

	/**
	 * Query the oddjobs depending on the tab currently selected.
	 * 
	 */
	$scope.queryJobs = function() {
		$scope.submitted = false;
		if ($scope.selectedTab == 'MAP') {
			$scope.queryAllJobs();
		} else if ($scope.selectedTab == 'LIST') {
			$scope.queryAllJobs();
		}
	};

	/**
	 * Invokes the oddjob.queryoddjobs API.
	 */
	$scope.queryAllJobs = function() {
		$scope.loading = true;
		gapi.client.oddjob.getAllJobsCreated().execute(function(resp) {
			$scope.$apply(function() {
				$scope.loading = false;
				if (resp.error) {
					// The request has failed.
					var errorMessage = resp.error.message || '';
					$scope.messages = 'Failed to query jobs : ' + errorMessage;
					$scope.alertStatus = 'warning';
				} else {
					// The request has succeeded.
					$scope.messages = 'Query succeeded : ';
					$scope.alertStatus = 'success';
					$log.info($scope.messages);
					console.log(resp.items);
					$scope.jobs = [];
					$scope.markers = [];
					angular.forEach(resp.items, function(job) {
						$scope.jobs.push(job);
						createMarker(job);
					});
				}
				$scope.submitted = true;
			});
		});
	}
});

/**
 * @ngdoc controller
 * @name JobDetailCtrl
 * 
 * @description A controller used for the job detail page.
 */
zachariliusApp.controllers.controller('JobDetailCtrl', function($scope, $log,
		$routeParams, HTTP_ERRORS) {
	$scope.job = {};

	$scope.isUserAttending = false;

	/**
	 * Initializes the oddjob detail page. Invokes the oddjob.getoddjob method
	 * and sets the returned oddjob in the $scope.
	 * 
	 */
	$scope.init = function() {
		$scope.loading = true;
		gapi.client.oddjob.getJob({
			websafeJobKey : $routeParams.websafeJobKey
		}).execute(
				function(resp) {
					$scope.$apply(function() {
						$scope.loading = false;
						if (resp.error) {
							// The request has failed.
							var errorMessage = resp.error.message || '';
							$scope.messages = 'Failed to get the job : '
									+ $routeParams.websafeKey + ' '
									+ errorMessage;
							$scope.alertStatus = 'warning';
							$log.error($scope.messages);
						} else {
							// The request has succeeded.
							$scope.alertStatus = 'success';
							console.log(resp.result);
							$scope.job = resp.result;
						}
					});
				});
	};

});
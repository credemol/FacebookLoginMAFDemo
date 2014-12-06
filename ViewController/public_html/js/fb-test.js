/*
(function() {
   alert('init'); 
  FB.init({
    appId      : '117708588361712',
    status     : true,
    xfbml      : true,
    version    : 'v2.0'
  });
});
*/

login = function() {
alert('my login');

 FB.login(function(response) {
   if (response.authResponse) {
     alert('Welcome!  Fetching your information.... ');
     FB.api('/me', function(response) {
       alert('Good to see you, ' + response.name + '.');
     });
   } else {
     alert('User cancelled login or did not fully authorize.');
   }
 });    
}
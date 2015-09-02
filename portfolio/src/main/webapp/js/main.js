$(document).ready(function() {
  /** Places a click listener on the elements with the .nav-elem class.
  * All elements when clicked scroll to their section on the page. -40 pixels
  */
  $('.nav-elem').on('click', function(e){
    var navTag = $(this).attr('href');
    var targetSection = $('html').find('div'+navTag);
    var targetLocation = targetSection.offset().top - 40;
    $('html, body').stop().animate({scrollTop: targetLocation}, 1000);
    e.preventDefault();
  });
  $('body').scrollspy({
    target: '#navbar',
    offset: 70
});
})

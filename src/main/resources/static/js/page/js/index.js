/* www.jq22.com */
$('#light-pagination').pagination({
	pages: 20,
	cssStyle: 'light-theme'
});
var page=5;
$('#dark-pagination').pagination({
	pages: 20,

	displayedPages: 6,
onPageClick: function(pageNumber, event) {
					// Callback triggered when a page is clicked
					// Page number is given as an optional parameter
alert(pageNumber);
				},
				onInit: function(id) {
					// Callback triggered immediately after initialization
alert(id);
				}


});

$('#compact-pagination').pagination({
	pages: 70,
	cssStyle: 'compact-theme',
	displayedPages: 7
});
/* www.jq22.com */
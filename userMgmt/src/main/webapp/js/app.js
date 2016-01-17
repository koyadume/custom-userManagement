/*
 * Copyright (c) 2012-2016 Shailendra Singh <shailendra_01@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var userMgmt = {
	tiles: []
};

$(function() {
	$('.pseudo-search-type').click(function() {
		var value = $('.pseudo-search-type:checked').val();
		var searchDropDown = $('.pseudo-search-dropdown');
		if(value == "USER") {
			searchDropDown.find('option').remove();
			searchDropDown.append('<option value="">--Select--</option>')
				.append('<option value="uid">UID</option>')
				.append('<option value="firstName">First Name</option>')
				.append('<option value="email">Email</option>');
        } else if(value == "GROUP") {
        	searchDropDown.find('option').remove();
        	searchDropDown.append('<option value="">--Select--</option>')
	  			.append('<option value="name">Name</option>');
        }
		
		$('.pseudo-attr-value').val('');
	});
	
	$('.pseudo-search').click(function() {
    	$('.pseudo-waitSearchResults').show();
    	$('.pseudo-searchResults').html('');
    	var form = getPluginFormWithAction($(this), 'searchResults');
    	$.ajax({
       		async : false,
            url: '/piston/ajaxPlugin',
            type: 'POST',
    		data: $(this).closest('form').serializeArray(),
            success: function(data) {
            	$('.pseudo-waitSearchResults').hide();
				$('.pseudo-searchResults').html(data);
            }
    	});
    });
	
	$('.pseudo-searchResults').on('click', '.pseudo-delete-principals', function() {
    	$('.pseudo-waitSearchResults').show();
    	$('.pseudo-searchResults').hide();
    	var form = getPluginFormWithAction($(this), 'deletePrincipals');
    	$.ajax({
       		async : false,
            url: '/piston/ajaxPlugin',
            type: 'POST',
    		data: form.serializeArray(),
            success: function(data) {
            	$('.pseudo-waitSearchResults').hide();
				$('.pseudo-searchResults').html(data);
				$('.pseudo-searchResults').show();
            }
    	});
    });
	
	$('.pseudo-add-member').click(function() {
    	targetRole = $(this).closest('tr').find('.pseudo-member-container');
    	var modal = $('.pseudo-full-modal');
    	modal.show();
    	
    	modal.css('top', $(window).scrollTop() + 'px');
    	$('html, body').css({
    	    'overflow': 'hidden'
    	});
    	
    	$.ajax({
    		async : false,
            url: '/piston/web/getAjaxView?view=searchUsersGroupsDB',
            type: 'GET',
            success: function(data) {
                modal.find('.pseudo-loader').hide();
                modal.find('.pseudo-full-modal-content').html(data);
            }
    	});
    });
    
    $('.horList1').on("click", '.prin-del', function(ev) {
    	var principal = $(this).prev().html().trim();
    	$('.pseudo-role').each(function() {
    	    var value = $(this).val().substr($(this).val().indexOf(':') + 1);
	    	if(value == principal) {
	    		$(this).remove();
	    		return false;
	    	} 
	    });
    	
    	$(this).parent().remove(); 
    });
});
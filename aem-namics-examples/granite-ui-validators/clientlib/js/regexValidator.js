jQuery.validator.register({
	selector : "[data-regex]",
	validate : function(el) {
		var regex = new RegExp(el.attr("data-regex"));
		var errorMessage = Granite.I18n.get(el.attr("data-regex-text"));

		if (el.val().match(regex) != null) {
			return;
		} else {
			return errorMessage;
		}
	}
});


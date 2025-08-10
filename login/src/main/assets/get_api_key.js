javascript:(function() {
    // Look for API key in various possible locations
    var apiKey = null;

    // Method 1: Look for API key input field
    var apiInput = document.querySelector('input[type="text"][readonly]');
    if (apiInput && apiInput.value && apiInput.value.length > 30) {
        apiKey = apiInput.value;
    }

    // Method 2: Look for API key in text content
    if (!apiKey) {
        var textContent = document.body.innerText;
        var match = textContent.match(/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}/);
        if (match) {
            apiKey = match[0];
        }
    }

    // Method 3: Look for API key in data attributes
    if (!apiKey) {
        var elements = document.querySelectorAll('[data-api-key]');
        if (elements.length > 0) {
            apiKey = elements[0].getAttribute('data-api-key');
        }
    }

    // Method 4: Look for common input patterns for API keys
    if (!apiKey) {
        var inputs = document.querySelectorAll('input[type="text"], input[type="password"]');
        for (var i = 0; i < inputs.length; i++) {
            var value = inputs[i].value;
            if (value && value.length > 30 && value.match(/[a-f0-9-]/)) {
                apiKey = value;
                break;
            }
        }
    }

    // Method 5: Look in code blocks or pre tags
    if (!apiKey) {
        var codeElements = document.querySelectorAll('code, pre');
        for (var i = 0; i < codeElements.length; i++) {
            var text = codeElements[i].innerText || codeElements[i].textContent;
            var match = text.match(/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}/);
            if (match) {
                apiKey = match[0];
                break;
            }
        }
    }

    return apiKey;
})()
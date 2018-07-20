var searchManager = (function () {
    var get = function(obj, callback) {
        $.ajax({
            type: 'get',
            url: '/course/' + obj.year + '/' + obj.semester + '/' + obj.code,
            success: callback
        });
    };

    return {
        get: get
    }
})();
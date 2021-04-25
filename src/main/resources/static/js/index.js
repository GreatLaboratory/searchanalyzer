function user_delete(uid) {
    if(uid==='admin') {
        alert('관리자는 삭제할 수 없습니다.')
    } else {
        $.ajax({
            type: 'DELETE',
            url: `/api/v1/user/${uid}`,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('사용자가 삭제되었습니다.');
            window.location.href = '/admin/users';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}

function user_search() {
    var keyword = $('#keyword').val();
    window.location.href = '/admin/users?keyword='+keyword
}

function host_search_function() {
    var host_keyword = $('#host_keyword').val();
    var sort = $('#sort').val();
    window.location.href = '/host?keyword=' + host_keyword + '&sort=' + sort
}

$("button").off().on("click", "button #keyword_search", function(){
    var keyword2 = $('#search_keyword').val()
    window.location.href = '/search?keyword='+keyword2
})
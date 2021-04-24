function user_delete(id) {
    if(id==1) {
        alert('관리자는 삭제할 수 없습니다.')
    } else {
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/user/'+id,
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

$("button").off().on("click","button #host_search",function(){
    var keyword = $('#keyword').val();
        document.location.href = '/host?keyword='+keyword
})

$("button").off().on("click","button #keyword_search",function(){
    var keyword = $('#keyword').val();
        document.location.href = '/search?keyword='+keyword
})
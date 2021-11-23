function initialize() {
    console.log(" init call ")
    const deleteButtons = $('.delete-transition-button')
    console.log(`size: ${deleteButtons.length}`)
    deleteButtons.click(function () {
        console.log('delete clicked')
        const transitionId = $(this).attr('transition-id')
        console.log(`transition id: ${transitionId}`)
        deleteTransition(transitionId)
    })
}
function deleteTransition(transitionId) {
    const url = `http://localhost:2990${AJS.contextPath()}/rest/myPlugin/1.0/transition/${transitionId}`
    console.log(`url: ${url}`)
    $.ajax({
        url: url,
        type: "DELETE",
        error: function (_, textStatus, errorThrown) {
            console.error(`delete failed: ${textStatus}, ${errorThrown}`)
        },
        success: function (data, textStatus) {
            console.log(`delete successful: ${data}, ${textStatus}`)
            location.reload()
        }
    });
}
$(initialize)
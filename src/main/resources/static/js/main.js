$(".clickable_btn").click(function () {
    let player = $(this).attr("player");
    let pitId = $(this).attr("pit-id");
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({"player": player, "pitId": pitId}),
        url: "/mancala/play",
    }).done(function (result) {
        let stoneCount = result.stones;
        let turn = result.turn;
        let winner = result.winner;
        for (let i = 0; i < stoneCount.length; i++) {
            $("#" + i).text(stoneCount[i]);
        }
        if (result.gameOver) {
            $(".clickable_btn").addClass("disabled");
            let text = winner != null ? winner + " win!" : "Draw!";
            $(".modal-title").text(text + " Score: " + stoneCount[6] + " - " + stoneCount[13]);
            $("#winner_modal").show();
            return;
        }
        $(".small-pit[player=" + turn + "]").removeAttr("disabled");
        $(".small-pit[player!=" + turn + "]").attr("disabled", "");
    });
});

$(".close_modal").click(function () {
    $("#winner_modal").hide();
})
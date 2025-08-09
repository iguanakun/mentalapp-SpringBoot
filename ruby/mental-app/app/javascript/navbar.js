function dropdown (button_id, lists_id, nav_target, bgcolor){
  // ドロップダウンボタンの取得
  const pullDownButton = document.getElementById(button_id);
  // ドロップダウンリストの取得
  const pullDownLists = document.getElementById(lists_id);

  // hoverで背景色を変更
  pullDownButton.addEventListener('mouseover', function(){
    pullDownButton.setAttribute("style", `background-color:${bgcolor};`)
  })
  pullDownButton.addEventListener('mouseout', function(){
    pullDownButton.removeAttribute("style");
  })

  // ドロップダウンメニューの表示と非表示の設定
  pullDownButton.addEventListener('click', function() {
    if (pullDownLists.style.display == 'block') {
      pullDownLists.style.display = 'none';
    } else {
      pullDownLists.style.display = 'block';
    }
  });

  // ドキュメント全体にクリックイベントハンドラを追加
  document.addEventListener('click', function(e) {
    // メニューの枠外をクリック時、メニューを閉じる
    if(e.target.closest(nav_target) === null) {
      pullDownLists.style.display = 'none';
    }
  });
};

function navbar (){
  /* エクササイズメニューの開閉 */
  dropdown ('navbar-items', 'navbar-pulldown','#navbar01','#96dc78');
  /* コースメニューの開閉 */
  dropdown ('navbar-items02', 'navbar-pulldown02','#navbar02','#e5e5e5');
  /* メモ一覧メニューの開閉 */
  // dropdown ('navbar-items03', 'navbar-pulldown03','#navbar03','#e5e5e5');
};

// window.addEventListener('turbo:load', navbar);
window.addEventListener('load', navbar);
window.addEventListener('turbo:render', navbar);

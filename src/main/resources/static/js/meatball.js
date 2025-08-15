function meatball (){
  const pullDownButton = document.getElementById("meatball-menu")
  const pullDownParents = document.getElementById("meatball-menu__lists");
  
  pullDownButton.addEventListener('click', function(e) {
    // プルダウメニューの表示と非表示の設定
    if (pullDownParents.style.display == 'block') {
        pullDownParents.style.display = 'none';
    } else {
        pullDownParents.style.display = 'block';
    }
    
    // クリックがプルダウンボタン内で伝播しないようにする
    e.stopPropagation();
  });

  // ドキュメント全体にクリックイベントハンドラを追加
  document.addEventListener('click', function() {
      // プルダウンメニューを非表示にする
      pullDownParents.style.display = 'none';
  });

  // プルダウンメニュー内のクリックがドキュメント全体に伝播しないようにする
  pullDownParents.addEventListener('click', function(e) {
      e.stopPropagation();
  });

};
 
window.addEventListener('turbo:load', meatball);
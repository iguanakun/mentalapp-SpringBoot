function setCheckBoxStyles(checkBox, isChecked) {
  if (isChecked) {
    checkBox.style.backgroundColor = '#ffffff';
    checkBox.style.color = 'gray';
    checkBox.style.boxShadow = '0 0 1.2px 1.2px #4FE358, 0 0 1.2px 1.2px #4FE358 inset';
  } else {
    checkBox.style.backgroundColor = '';
    checkBox.style.color = '';
    checkBox.style.boxShadow = 'none';
  }
}

function feel_checkbox() {
  // チェックボックス要素の取得
  const checkBoxes = document.getElementsByClassName('checkbox');
  // // 感情の種類テキストの取得
  // const feelTitles = document.getElementsByClassName('feel-kinds');
  // // 感情メニューの取得
  // const feelMenus = document.getElementsByClassName('feel-kinds__menu');

  for (let i = 0; i < checkBoxes.length; i++) {
    const checkBox = checkBoxes[i];
    const inputElement = checkBox.querySelector('input'); // <input>要素を取得

    // ページが読み込まれたときの処理
    const isChecked = inputElement.checked;
    setCheckBoxStyles(checkBox, isChecked);

    // チェックボックスの変化時の処理
    checkBox.addEventListener('change', function() {
      const isChecked = inputElement.checked;
      setCheckBoxStyles(checkBox, isChecked);
    });

    // hoverで背景色を変更
    checkBox.addEventListener('mouseover', function() {
      if (!inputElement.checked) {
        checkBox.style.backgroundColor = '#c9f29b';
      }
    });
    checkBox.addEventListener('mouseout', function() {
      if (!inputElement.checked) {
        checkBox.style.backgroundColor = '';
      }
    });
  }

  // for (let i = 0; i < feelTitles.length; i++) {
  //   const feelTitle = feelTitles[i];
  //   const feelMenu = feelMenus[i];
 
  //   // 感情の種類テキストをクリックで開閉
  //   feelTitle.addEventListener('click', function() {
  //     if (feelMenu.style.display === 'block') {
  //       console.log(feelMenu);
  //       console.log(`${i}:none`);
  //       feelMenu.style.display = 'none';
  //     } else {
  //       console.log(feelMenu);
  //       console.log(`${i}:block`);
  //       feelMenu.style.display = 'block';
  //       console.log(feelMenu);
  //     }
  //   });
  // }
};

window.addEventListener('load', feel_checkbox);
window.addEventListener('turbo:render', feel_checkbox);

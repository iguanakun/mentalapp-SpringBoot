/**
 * 削除確認モーダル
 */
function initDeleteModal() {
  const modal = document.getElementById('deleteModal');
  const cancelBtn = document.getElementById('modalCancelBtn');
  const confirmBtn = document.getElementById('modalConfirmBtn');
  const overlay = modal?.querySelector('.modal-overlay');

  // 削除ボタンクリック時の処理
  let targetForm = null;

  /**
   * モーダルを表示する
   * @param {HTMLFormElement} form - 削除対象のフォーム
   */
  function showModal(form) {
    if (!modal) return;

    targetForm = form;
    modal.style.display = 'flex';
    modal.classList.add('show');

    // bodyのスクロールを無効化
    document.body.style.overflow = 'hidden';
  }

  /**
   * モーダルを閉じる
   */
  function closeModal() {
    if (!modal) return;

    modal.classList.remove('show');

    // アニメーション後に非表示
    setTimeout(() => {
      modal.style.display = 'none';
      targetForm = null;

      // bodyのスクロールを有効化
      document.body.style.overflow = '';
    }, 200);
  }

  /**
   * 削除を実行する
   */
  function executeDelete() {
    if (targetForm) {
      targetForm.submit();
    }
    closeModal();
  }

  // イベントリスナー設定
  if (cancelBtn) {
    cancelBtn.addEventListener('click', closeModal);
  }

  if (confirmBtn) {
    confirmBtn.addEventListener('click', executeDelete);
  }

  if (overlay) {
    overlay.addEventListener('click', closeModal);
  }

  // Escapeキーでモーダルを閉じる
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && modal && modal.style.display === 'flex') {
      closeModal();
    }
  });

  // 全ての削除ボタンにイベントを設定
  const deleteButtons = document.querySelectorAll('.delete-button');
  deleteButtons.forEach(button => {
    button.addEventListener('click', (e) => {
      e.preventDefault();
      e.stopPropagation();

      // 親フォームを取得
      const form = button.closest('form');
      if (form) {
        showModal(form);
      }
    });
  });
}

// ページ読み込み時に初期化
window.addEventListener('turbo:load', initDeleteModal);
window.addEventListener('DOMContentLoaded', initDeleteModal);
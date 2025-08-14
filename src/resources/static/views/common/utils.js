const utils = {
  /**打开外部新页面*/
  openNewWindow(url) {
    const tempA = document.createElement("a")
    tempA.setAttribute("target", "_blank")
    tempA.setAttribute("href", url)
    tempA.click()
    tempA.remove()
  },
  /**通用复制文本*/
  copyText(text) {
    if (navigator.clipboard && window.isSecureContext) {
      return navigator.clipboard.writeText(text)
    } else {
      const textArea = document.createElement('textarea')
      textArea.value = text
      document.body.appendChild(textArea)
      textArea.focus()
      textArea.select()
      return new Promise((res, rej) => {
        document.execCommand('copy') ? res() : rej()
        textArea.remove()
      })
    }
  },
  /** html转义为正常字符串 */
  html2Escape(sHtml) {
    return sHtml.replace(/[<>&"]/g,
      function (c) {return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c]},
    )
  },
  /** 内部打开新页面 iframe */
  openNewPage([title, url]) {
    layer.open({
      title: title, type: 2, shade: 0.2,
      maxmin: true, move: false, skin: 'self-class',
      shadeClose: true, area: ['90%', '90%'],
      content: ctxUrl + url,
    })
  },
  /** Loading消息触发 */
  loadingFun(message = "加载中...") {
    return layer.load(2, {
      shade: [0.1, '#000'],
      success($layer) {
        $layer.css({
          display: "flex", "align-items": "center",
          "flex-direction": "column", "justify-content": "center",
        })
        $layer.append(`<b>${ message }</b>`)
      },
    })
  },
  /**
   * 获取URL中的哈希值
   * @return {number | number}
   */
  getHashNumber() {
    const hash = window.location.hash
    return hash ? Number(hash.substring(2)) : 0 // 去掉前面的#符号
  },
}
// tmpl/posts.js
;(() => {
  // console.log(Vditor)
  // Vditor.mermaidRender(document.querySelector('.post-content'),'/static/vditor','light')
  Vditor.preview(document.querySelector('.post-content'), test.slice(0, 400), {
      cdn: "/static/vditor", anchor: 0, theme: {
        current: 'ant-design',
      },
      hljs: {
        style: 'github-dark-dimmed',
      }, after() {
        console.log(1231321)
        Vditor.outlineRender(document.querySelector('.post-content'), document.querySelector('.sidebar-toc'))
        // outlineRenderEle.querySelectorAll('span[data-target-id]').forEach(function (parEle) {
        //   parEle.querySelector('span').addEventListener('click', function (event) {
        //     event.preventDefault() // 阻止默认行为
        //     const tarEle = document.getElementById(parEle.getAttribute('data-target-id'))
        //       , contEle = previewEle.querySelector('.content')
        //     // if (targetElement) {targetElement.querySelector('a').click()}
        //     if (tarEle && tarEle.offsetTop !== contEle.scrollTop) {contEle.scrollTo({top: tarEle.offsetTop, behavior: 'smooth'})}
        //   })
        // })
      },
    },
  )
  // TODO 渲染 大纲 toc
})()

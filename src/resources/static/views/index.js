/**
 * @external dbJson
 * @type {obj}
 */
;(function () {
  "use strict"
  const element = layui.element, layer = layui.layer, $ = layui.$
  document.getElementById("my-nav").innerHTML = getSideHtml(dbJson.menuData).data
  element.render('nav', 'my-nav')
  // 导航点击事件注册
  element.on('nav(my-nav)', function (elem) {
    const {id, path, link} = sideItemVal(this)
    if (!path) { return }
    if (true === link) {
      utils.openNewWindow(path)
    } else {
      $(".admin-main .admin-content").load(ctxUrl + `${ path }`)
      breadcrumbHandle(Number(id))
      element.render('breadcrumb', 'my-breadcrumb')
      hashHrefUpdate(id)
      layer.msg(elem.text(), {time: 1000})
    }
  })
  clickActiveNav(dbJson.initPageId)
  // clickActiveNav(utils.getHashNumber())
  changeTheme(['blue-theme', 'green-theme', 'white-theme'])
})()
/**
 * 激活侧边栏 默认0首页
 * @param {number} id
 */
function clickActiveNav(id = 0) {
  /** @type {HTMLElement} */
  const idItem = document.querySelector(`.layui-nav a[data-id="${ id }"]`)
  idItem.click()
}
/**
 * 通过id来渲染 面包屑
 * @param id
 * @param element
 */
function breadcrumbHandle(id = 0) {
  const menuFind = findMenuById(dbJson.menuData, id)
  let templateHTML = ''
  menuFind.parentNames.forEach(item => {
    templateHTML += `<a><cite>${ item }</cite></a>`
  })
  templateHTML += `<a><cite>${ menuFind.name }</cite></a>`
  document.querySelector('.admin-main .admin-breadcrumb').innerHTML = templateHTML
}
/**
 * 通过id修改href的hash
 * @param id
 */
function hashHrefUpdate(id = 0) {
  location.hash = '#/' + id
}
/**
 * 随机改变主题事件
 * @param {String[]} themeList
 */
function changeTheme(themeList = []) {
  document.querySelector('.admin-sidebar .admin-logo').addEventListener('click', function () {
    const number = Math.floor(Math.random() * (themeList.length + 1)) - 1
    if (number === -1) {
      document.body.className = ''
    } else {
      document.body.className = themeList[number]
    }
  })
}
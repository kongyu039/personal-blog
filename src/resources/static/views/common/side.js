// src/resources/static/views/common/side.js
/**
 * menu数据转为HTML
 * @param dataList
 * @param isRoot
 * @return {{code: number, data: string, message: string}}
 */
function getSideHtml(dataList, isRoot = true) {
  const result = {code: -1, message: "传入数组为空！", data: ""}
  if (!(dataList && dataList.length !== 0)) return result
  result.code = 1
  result.message = "成功渲染侧边栏"
  const tempVoid = "href=javascript:void(0);"
  if (isRoot) {
    for (let i = 0; i < dataList.length; i++) {
      const {id, name, path, isExtLink, icon, children} = dataList[i]
      let childrenFlag = (children && children.length > 0)
      result.data += `<li class="layui-nav-item layui-nav-itemed">`
      result.data +=
        `<a data-id="${ id }" ${ !!path ? "data-path=" + path : '' } data-link="${ isExtLink }" ${ tempVoid } ${ childrenFlag ? 'style="pointer-events: none"' : '' }><i class="fa ${ icon }"></i>&ensp;<span>${ name }</span></a>`
      if (childrenFlag) {
        const resHtml = this.getSideHtml(children, false)
        result.data += resHtml.data
      }
      result.data += `</li>`
    }
  } else {
    for (let i = 0; i < dataList.length; i++) {
      const {id, name, path, isExtLink, icon, children} = dataList[i]
      let childrenFlag = (children && children.length > 0)
      result.data += `<dl class="layui-nav-child">`
      result.data += `<dd>`
      result.data +=
        `<a data-id="${ id }" ${ !!path ? "data-path=" + path : '' } data-link="${ isExtLink }" ${ tempVoid } ${ childrenFlag ? 'style="pointer-events: none"' : '' }><i class="fa ${ icon }"></i>&ensp;<span>${ name }</span></a>`
      if (childrenFlag) {
        const resHtml = this.getSideHtml(children, false)
        result.data += resHtml.data
      }
      result.data += `</dd>`
      result.data += `</dl>`
    }
  }
  return result
}
/**
 * 侧边栏的menu值提取
 * @param {HTMLElement} elem
 * @return {{path: string, link: boolean, id: number}}
 */
function sideItemVal(elem) {
  const res = {id: 0, path: '', link: true}
  res.id = elem.dataset['id'] || 0
  res.path = elem.dataset['path'] || ''
  res.link = elem.dataset['link'] === 'true'
  return res
}
/**
 * 寻找父级标签
 * @param menuData 菜单数据
 * @param id  唯一id
 * @param parentNames 父名称数组
 */
function findMenuById(menuData, id, parentNames = []) {
  for (const item of menuData) {
    // 如果找到目标项
    if (item.id === id) {
      return {
        name: item.name,
        parentNames: parentNames,
      }
    }
    // 如果有子项，继续查找
    if (item.children) {
      const result = findMenuById(item.children, id, [...parentNames, item.name])
      if (result) {
        return result
      }
    }
  }
  return null // 如果没有找到，返回 null
}


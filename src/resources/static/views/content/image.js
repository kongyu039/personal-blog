// src/resources/static/views/content/image.js
;(function () {
  layui.use(['table', 'jquery'], function () {
    const table = layui.table, $ = layui.jquery
    renderTable(table, $)
  })
  /**
   * 渲染Table表格
   * @param {Layui.Table} table
   * @param {JQueryStatic} $
   */
  function renderTable(table, $) {
    // 表格数据url渲染
    /** @type Layui.TableColumnOptions[] */
    const recycleCols = [{field: 'id', title: 'ID', width: 90, unresize: true}
      , {
        field: 'key', title: '略缩图', width: 150, align: 'center',
        templet(d) { return '<img src="' + d.key + '" ' + 'alt="" height="100px">' },
      }
      , {
        field: 'key', title: '项目Key', unresize: true,
        templet(d) {return `<a href="${ d['key'] }" target="_blank" >${ d['key'] }</a>`},
      }
      , {
        field: 'flag', title: '标记', width: 110, unresize: true
        , templet(d) {return d['flag'] === 1 ? "外链" : "本地"},
      }
      , {title: '操作', width: 120, align: 'center', templet: '#image-tool', fixed: 'right', unresize: true}]
    // noinspection JSValidateTypes
    /** @type layui.TableOptions */
    const tagOptions = {
      elem: '#image-table', url: ctxUrl + 'image/list-image', method: 'GET', cols: [recycleCols],
      defaultToolbar: false, response: {statusCode: 200}, page: true, limit: 10, limits: [10, 15, 20],
      parseData(res) {
        return {code: res.code, msg: res.msg, count: res.data.length, data: res.data}
      },
    }
    /** @type {Layui.TableRenderReturn} */
    const imageTable = table.render(tagOptions)
    // 行工具事件
    table.on(`tool(image-table)`, function (obj) {
      const {data: {id, key}} = obj
      layer.confirm(`<div style="text-align: center">确认彻底删除「${ key }」</div>`, function (index) {
        $.ajax({
          url: ctxUrl + 'image/del-image', type: 'post', data: {imageId: id}, traditional: true, success(res) {
            if (res.code === 200) {
              imageTable.reload()
              layer.close(index)
            }
          }, error(res) {
            console.error(res)
            layer.close(index)
          },
        })
      })
    })
  }
})()
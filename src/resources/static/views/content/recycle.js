// src/resources/static/views/content/recycle.js
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
    const recycleCols = [{checkbox: true, fixed: 'left', unresize: true}, {field: 'id', title: 'ID', width: 140, unresize: true},
      {field: 'cover', title: '封面', width: 80, hide: true, unresize: true}, {field: 'title', title: '标题', width: 230, unresize: true},
      {field: 'summary', title: '摘要', unresize: true},
      {field: 'category', title: '分类', width: 110, unresize: true}, {field: 'tags', title: '标签', unresize: true},
      {title: '创/更时间', width: 180, unresize: true, templet: "更:{{=d.updatedAt}}<br/>创:{{=d.createdAt}}"},
      {title: '操作', width: 120, align: 'center', templet: '#recycle-tool', fixed: 'right', unresize: true}]
    // noinspection JSValidateTypes
    /** @type layui.TableOptions */
    const tagOptions = {
      elem: '#recycle-table', url: ctxUrl + 'post/recycle-posts', method: 'GET', cols: [recycleCols], toolbar: '#recycle-toolbar',
      defaultToolbar: false, response: {statusCode: 200}, page: true, limit: 10, limits: [10, 15, 20],
      parseData(res) {
        const data = res.data.map(item => ({
          title: item.title, summary: item.summary,
          id: item['id'], cover: item['cover'], createdAt: item['createdAt'], updatedAt: item['updatedAt'],
          category: item['category']['name'], tags: item['tags'].map(item => item.name).join(','),
        }))
        return {code: res.code, msg: res.msg, count: res.data.length, data: data}
      },
    }
    /** @type {Layui.TableRenderReturn} */
    const recycleTable = table.render(tagOptions)
    // 工具栏工具
    table.on(`toolbar(recycle-table)`, function (obj) {
      const {event} = obj
      const ids = table.checkStatus('recycle-table').data.map(item => item.id),
        names = table.checkStatus('recycle-table').data.map(item => item.title).join(',')
      switch (event) {
        case 'restore':
          layer.confirm(`<div style="text-align: center">确认恢复「${ names }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'post/edit-post-del', type: 'post', data: {ids: ids, flag: false}, traditional: true, success(res) {
                if (res.code === 200) {
                  recycleTable.reload()
                  layer.close(index)
                }
              }, error(res) {
                console.error(res)
                layer.close(index)
              },
            })
          })
          break
        case 'delete':
          layer.confirm(`<div style="text-align: center">确认彻底删除「${ names }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'post/del-post', type: 'post', data: {ids: ids}, traditional: true, success(res) {
                if (res.code === 200) {
                  recycleTable.reload()
                  layer.close(index)
                }
              }, error(res) {
                console.error(res)
                layer.close(index)
              },
            })
          })
          break
      }
    })
    // 行工具事件
    table.on(`tool(recycle-table)`, function (obj) {
      const {event, data: {id, title}} = obj
      switch (event) {
        case 'rowRestore':
          layer.confirm(`<div style="text-align: center">确认恢复「${ title }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'post/edit-post-del', type: 'post', data: {ids: [id], flag: false}, traditional: true, success(res) {
                if (res.code === 200) {
                  recycleTable.reload()
                  layer.close(index)
                }
              }, error(res) {
                console.error(res)
                layer.close(index)
              },
            })
          })
          break
        case 'rowDelete':
          layer.confirm(`<div style="text-align: center">确认彻底删除「${ title }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'post/del-post', type: 'post', data: {ids: [id]}, traditional: true, success(res) {
                if (res.code === 200) {
                  recycleTable.reload()
                  layer.close(index)
                }
              }, error(res) {
                console.error(res)
                layer.close(index)
              },
            })
          })
          break
      }
    })
  }
})()
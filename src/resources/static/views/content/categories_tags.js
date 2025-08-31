;(function () {
  layui.use(['form', 'jquery', 'table', 'xmSelect', 'upload', 'element'], function () {
    const $ = layui.jquery, table = layui.table, form = layui.form
    renderTable(table, form, $, "category-table")
    renderTable(table, form, $, "tag-table")
  })

  /**
   * 渲染Table表格
   * @param {Layui.Table} table
   * @param {Layui.Form} form
   * @param {JQueryStatic} $
   * @param {string} tableName
   */
  function renderTable(table, form, $, tableName) {
    /** @type {Layui.TableRenderReturn} */
    let categoryTable, tagTable
    // 表格数据url渲染
    switch (tableName) {
      case 'category-table':
        const categoryCols = [{checkbox: true, fixed: 'left', unresize: true}
          , {field: 'id', title: 'ID', width: 70, align: 'right', unresize: true}
          , {field: 'name', title: "名称", width: 140, edit: 'text', unresize: true}
          , {field: 'summary', title: '概括', edit: 'text', unresize: true}
          , {
            title: '操作', width: 120, align: 'center', fixed: 'right', unresize: true,
            templet: '<button class="layui-btn layui-btn-sm layui-bg-purple" lay-event="del">删除</button>',
          }]
        categoryTable = table.render({
          elem: '#category-table',
          url: ctxUrl + 'category/all-list', method: 'GET',
          cols: [categoryCols], toolbar: '#operate-toolbar', defaultToolbar: false,
          height: '', editTrigger: 'dblclick', response: {statusCode: 200},
          page: true, limit: 10, limits: [10, 15, 20], parseData(res) {
            return {code: res.code, msg: res.msg, count: res.data.length, data: res.data}
          },
        })
        break
      case 'tag-table':
        const tagCols = [{checkbox: true, fixed: 'left', unresize: true}
          , {field: 'id', title: 'ID', width: 70, align: 'right', unresize: true}
          , {field: 'name', title: "名称", edit: 'text', unresize: true}
          , {
            title: '操作', width: 120, align: 'center', fixed: 'right', unresize: true,
            templet: '<button class="layui-btn layui-btn-sm layui-bg-purple" lay-event="del">删除</button>',
          }]
        tagTable = table.render({
          elem: '#tag-table',
          url: ctxUrl + 'tag/all-list', method: 'GET',
          cols: [tagCols], toolbar: '#operate-toolbar', defaultToolbar: false,
          height: '', editTrigger: 'dblclick', response: {statusCode: 200},
          page: true, limit: 10, limits: [10, 15, 20], parseData(res) {
            return {code: res.code, msg: res.msg, count: res.data.length, data: res.data}
          },
        })
        break
    }
    // 工具栏工具
    table.on(`toolbar(${ tableName })`, function (obj) {
      const {event} = obj
      const ids = table.checkStatus(tableName).data.map(item => item.id)
      const names = table.checkStatus(tableName).data.map(item => item.name).join(',')
      switch (tableName) {
        case 'category-table':
          switch (event) {
            case 'add':
              layer.open({
                title: "新增分类", type: 1, area: ['400px', '230px'], resize: false, move: false,
                content: document.querySelector('#category-add').innerHTML,
                btn: ['提交', '取消'],
                success() { form.render(null, 'form-category-add')},
                yes(index) {
                  form.submit('form-category-add', (data) => {
                    $.ajax({
                      url: ctxUrl + 'category/add-category',
                      type: 'post',
                      data: data.field,
                      success(res) {
                        if (res.code === 200) {
                          categoryTable.reload()
                          layer.close(index)
                        }
                      },
                    })
                  })
                },
              })
              break
            case 'batchDel':
              layer.confirm(`<div style="text-align: center">确认删除「${ names }」</div>`, function (index) {
                $.ajax({
                  url: ctxUrl + 'category/del-category',
                  type: 'post', data: {ids: ids},
                  traditional: true,
                  success(res) {
                    if (res.code === 200) {
                      categoryTable.reload()
                      layer.close(index)
                    }
                  },
                  error(res) {
                    layer.close(index)
                  },
                })
              })
              break
          }
          break
        case 'tag-table':
          switch (event) {
            case 'add':
              layer.open({
                title: "新增标签", type: 1, area: ['400px', '180px'], resize: false, move: false,
                content: document.querySelector('#tag-add').innerHTML,
                btn: ['提交', '取消'],
                success() { form.render(null, 'form-tag-add')},
                yes(index) {
                  form.submit('form-tag-add', (data) => {
                    $.ajax({
                      url: ctxUrl + 'tag/add-tag',
                      type: 'post',
                      data: data.field,
                      success(res) {
                        tagTable.reload()
                        layer.close(index)
                      },
                    })
                  })
                },
              })
              break
            case 'batchDel':
              layer.confirm(`<div style="text-align: center">确认删除「${ names }」</div>`, function (index) {
                $.ajax({
                  url: ctxUrl + 'tag/del-tag',
                  type: 'post', data: {ids: ids},
                  traditional: true,
                  success(res) {
                    if (res.code === 200) {
                      tagTable.reload()
                      layer.close(index)
                    }
                  },
                  error(res) {
                    layer.close(index)
                  },
                })
              })
              break
          }
          break
      }
    })
    // 行工具事件
    table.on(`tool(${ tableName })`, function (obj) {
      const {id, name} = obj.data
      switch (tableName) {
        case 'category-table':
          layer.confirm(`<div style="text-align: center">确认删除「${ name }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'category/del-category',
              type: 'post', data: {ids: [id]},
              traditional: true,
              success(res) {
                if (res.code === 200) {
                  categoryTable.reload()
                  layer.close(index)
                }
              },
              error(res) {
                layer.close(index)
              },
            })
          })
          break
        case 'tag-table':
          layer.confirm(`<div style="text-align: center">确认删除「${ name }」</div>`, function (index) {
            $.ajax({
              url: ctxUrl + 'tag/del-tag',
              type: 'post', data: {ids: [id]},
              traditional: true,
              success(res) {
                if (res.code === 200) {
                  tagTable.reload()
                  layer.close(index)
                }
              },
              error(res) {
                layer.close(index)
              },
            })
          })
          break
      }
    })
    // 编辑事件
    table.on(`edit(${ tableName })`, function (obj) {
      const {id, name, summary} = obj.data
      switch (tableName) {
        case 'category-table':
          $.ajax({
            url: ctxUrl + 'category/edit-category',
            type: 'post', data: {id, name, summary},
            success(res) {
              if (res.code === 200) {layer.msg("更新成功", {time: 1000})}
            },
          })
          break
        case 'tag-table':
          $.ajax({
            url: ctxUrl + 'tag/edit-tag',
            type: 'post', data: {id, name},
            success(res) {
              if (res.code === 200) { layer.msg("更新成功", {time: 1000})}
            },
          })
          break
      }
    })
  }
})()
<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="名称（标题）">
              <a-input placeholder="请输入名称（标题）" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="链接地址">
              <a-input placeholder="请输入链接地址" v-model="queryParam.url"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="图片">
              <a-input placeholder="请输入图片" v-model="queryParam.img"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="类型：1.首页焦点图2.专利奖展示3.信息公开4.法规政策5.维权援助6.明星企业7.联系我们8.国家级优势企业9.省级优势企业10.市级优势企业">
              <a-input placeholder="请输入类型：1.首页焦点图2.专利奖展示3.信息公开4.法规政策5.维权援助6.明星企业7.联系我们8.国家级优势企业9.省级优势企业10.市级优势企业" v-model="queryParam.type"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="是否有效 1有效0无效">
              <a-input placeholder="请输入是否有效 1有效0无效" v-model="queryParam.status"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('资讯配置')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <sysInformation-modal ref="modalForm" @ok="modalFormOk"></sysInformation-modal>
  </a-card>
</template>

<script>
  import SysInformationModal from './modules/SysInformationModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "SysInformationList",
    mixins:[JeecgListMixin],
    components: {
      SysInformationModal
    },
    data () {
      return {
        description: '资讯配置管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '名称（标题）',
            align:"center",
            dataIndex: 'name'
           },
		   {
            title: '链接地址',
            align:"center",
            dataIndex: 'url'
           },
		   {
            title: '图片',
            align:"center",
            dataIndex: 'img'
           },
		   {
            title: '类型：1.首页焦点图2.专利奖展示3.信息公开4.法规政策5.维权援助6.明星企业7.联系我们8.国家级优势企业9.省级优势企业10.市级优势企业',
            align:"center",
            dataIndex: 'type'
           },
		   {
            title: '是否有效 1有效0无效',
            align:"center",
            dataIndex: 'status'
           },
		   {
            title: '内容',
            align:"center",
            dataIndex: 'content'
           },
		   {
            title: '备注',
            align:"center",
            dataIndex: 'remark'
           },
		   {
            title: '来源',
            align:"center",
            dataIndex: 'source'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/system/sysInformation/list",
          delete: "/system/sysInformation/delete",
          deleteBatch: "/system/sysInformation/deleteBatch",
          exportXlsUrl: "system/sysInformation/exportXls",
          importExcelUrl: "system/sysInformation/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>